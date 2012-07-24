#include <stdio.h>
#include "SidPCMInterface.hpp"
#include "SidPlatform.hpp"
#ifndef _WIN32
#include <sys/times.h>
#include <unistd.h>
#endif

#include "PCMLoopbackOptionsParser.hpp"

static const char* deviceNames[] = {"INPUT_DEVICE", "OUTPUT_DEVICE", "NOTIFICATION_DEVICE"};

#define SAMPLE_RATE		44100
//#define USE_SINGLE_CALLBACK	1


int clocks_per_second = 0;
unsigned int get_milliseconds()
{
#ifdef _WIN32
	return GetTickCount();
#else
	if(clocks_per_second == 0)
	{
	  #ifdef _SC_CLK_TCK
	      clocks_per_second = sysconf(_SC_CLK_TCK);
	      if (clocks_per_second <= 0)
	  #endif
		  clocks_per_second = CLOCKS_PER_SEC;
	}
	struct tms t;
	int rv = ((unsigned long long)(unsigned int)times(&t)) * 1000 / clocks_per_second;
	
	return rv;
#endif
}

struct DevInfo
{
    Sid::String guid;
    Sid::String name;
    Sid::String productID;
};

class AudioFrame
{
	uint SizeInBytes() const
	{
		return m_sampleRate / 100 * m_channels * sizeof(short);
	}
  public:
	Sid::Binary m_data;
	int m_sampleRate;
	int m_channels;
	
	AudioFrame():
	    m_sampleRate(SAMPLE_RATE),
	    m_channels(1)
	{}
	    
	AudioFrame(const Sid::Binary& data, int sampleRate, int channels):
		m_data(data),m_sampleRate(sampleRate), m_channels(channels)
	{
		if(m_data.size() != SizeInBytes())
			m_data.resize(SizeInBytes());
	}
	
	void Clear(){ if(m_data.data()) memset(m_data.data(),0,m_data.size());}
	void Reset(){ m_data.resize(0);}
	
	void CopyAudioData(const AudioFrame& inputFrame)
	{
		if(m_data.size() != SizeInBytes())
			m_data.resize(SizeInBytes());
		if(inputFrame.m_data.size() != inputFrame.SizeInBytes())
		{
			Clear();
			return;
		}
		
		if(m_sampleRate != inputFrame.m_sampleRate)
		{
			Clear();
			return;
		}
		
		if(m_channels == inputFrame.m_channels)
		{
			  memcpy(m_data.data(), inputFrame.m_data.data(), SizeInBytes());
		}
		else if(m_channels == 1 && inputFrame.m_channels == 2)
		{
			short* inData = (short*) inputFrame.m_data.data();
			short* outData = (short*) m_data.data();
			for(int i = 0; i < m_sampleRate / 100; i++)
				outData[i] = inData[i * 2];
		}
		else if(m_channels == 2 && inputFrame.m_channels == 1)
		{
			short* inData = (short*) inputFrame.m_data.data();
			short* outData = (short*) m_data.data();
			for(int i = 0; i < m_sampleRate / 100; i++)
			{
				outData[i*2] = inData[i];
				outData[i*2 + 1] = inData[i];
			}
		}
		else
		{
			Clear();
		}
	}
};

#define MAX_FRAMES 	201
class LoopbackBuffer
{
	int m_delay_frames; // delay in audio frames
	uint m_loopback_pointer;
	AudioFrame m_frames[MAX_FRAMES];
  public:
	LoopbackBuffer():m_delay_frames(20), m_loopback_pointer(MAX_FRAMES - 1)
	{
	}
	void Push(const AudioFrame & frame)
	{
		m_loopback_pointer ++;
		int pos = m_loopback_pointer % MAX_FRAMES;
		m_frames[pos] = frame;
	}
	
	void Pull(AudioFrame& frame)
	{
		int pos = (m_loopback_pointer - m_delay_frames) % MAX_FRAMES;
		frame.CopyAudioData(m_frames[pos]);
	}
	
	bool SetDelay(int delay_ms)
	{
		if(delay_ms / 10 > MAX_FRAMES - 1 || delay_ms < 0)
			return false;
		m_delay_frames = delay_ms / 10;
		return true;
	}
	
	void Clear()
	{
		for(int i = 0; i < MAX_FRAMES; i++)
			m_frames[i].Clear();
	}
	  
};

class PCMLoopback : public SkypePCMInterface, public Sid::Thread{
      int current_device[3];
      DevInfo devices[3];
    public:


	PCMLoopback(SkypePCMCallbackInterface* transport, PCMLoopbackOptionsParser* options):
	  input_started(0), output_started(0), notification_started(0),
	  input_muted(0), output_muted(0),
	  input_volume(100), output_volume(100),
	  input_sampleRate(SAMPLE_RATE), 
	  output_sampleRate(SAMPLE_RATE),notification_sampleRate(SAMPLE_RATE),
	  output_channels(1),input_channels(1),
	  forceInputChannels(-1),forceOutputChannels(-1),forceSampleRate(-1),
	  m_transport(transport),
	  Stopping(false)
	{
		devices[0].guid = "guid0";
		devices[1].guid = "9";
		devices[2].guid = "guid10";

		devices[0].name = "DefaultDevice";
		devices[1].name = "Dev9";
		devices[2].name = "Dev10";

		devices[0].productID = "productID 1";
		devices[1].productID = "productID 2";
		devices[2].productID = "productID 3";

		current_device[0] = 0;
		current_device[1] = 0;
		current_device[2] = 0;
		
		if(options)
		{
			forceInputChannels = options->m_forceInputChannels;
			forceOutputChannels = options->m_forceOutputChannels;
			forceSampleRate = options->m_forceSampleRate;
			loopback_buf.SetDelay(options->m_delay);
		}
		if(forceInputChannels > 0)
			input_channels = forceInputChannels;
		if(forceOutputChannels > 0)
			output_channels = forceOutputChannels;
		if(forceSampleRate > 0)
		{
			input_sampleRate = forceSampleRate;
			output_sampleRate = forceSampleRate;
			notification_sampleRate = forceSampleRate;
		}
      }
  
	virtual ~PCMLoopback() 
	{
	}
	
	virtual int Init()
	{ 
	  SID_INFO("Init"); 
	  Sid::Thread::start("callbackthread");
	  return 0;
	}

	virtual int Start(int deviceType)
	{
	  SID_INFO("Start: %s", deviceNames[deviceType]);
	  if(deviceType == INPUT_DEVICE)	
	  {
	    input_started = 1;
	  }
	  else if(deviceType == OUTPUT_DEVICE)
	  {
	    output_started = 1;
	  }
	  else if(deviceType == NOTIFICATION_DEVICE)
	  {
	    notification_started = 1;
	  }
	  return PCMIF_OK;
	}

	// Hint for the compiler so the paramless overload can be found.
	using Sid::Thread::Stop;

	virtual int Stop(int deviceType){
	  SID_INFO("Stop: %s", deviceNames[deviceType]);
	  if(deviceType == INPUT_DEVICE)
	    input_started = 0;
	  if(deviceType == OUTPUT_DEVICE)
	    output_started = 0;
	  if(deviceType == NOTIFICATION_DEVICE)
	    notification_started = 0;
	  return PCMIF_OK;
	}

	virtual int GetDefaultDevice(int deviceType, Sid::String& guid, Sid::String& p_name, Sid::String& productID) 
	{
	  (void)deviceType;
	  guid = devices[0].guid;
	  p_name = devices[0].name;
	  productID = devices[0].productID;
	  return 0;
	}
  
	virtual int UseDefaultDevice(int deviceType) 
	{
	  SID_INFO("UseDefaultDevice: %s", deviceNames[deviceType]);
	  current_device[deviceType] = 0;
	  return PCMIF_OK;
	}

	virtual int GetCurrentDevice(int deviceType, Sid::String& guid, Sid::String& p_name, Sid::String& productID) 
	{
	  guid = devices[current_device[deviceType]].guid;
	  p_name = devices[current_device[deviceType]].name;
	  productID = devices[current_device[deviceType]].productID;
	  SID_INFO("GetCurrentDevice: %s, %s",deviceNames[deviceType], guid.data());
	  return PCMIF_OK;
	}

	virtual int UseDevice(int deviceType, const Sid::String& guid) 
	{
	  SID_INFO("UseDevice: %s, %s", deviceNames[deviceType], guid.data());
	  for (uint i = 0; i < sizeof(devices)/sizeof(devices[0]); ++i)
	  {
	    if (devices[i].guid == guid)
	    {
	      current_device[deviceType] = i;
	      return PCMIF_OK;
	    }
	  }
	  return PCMIF_ERROR;
	}

	virtual int GetDeviceCount(int deviceType, uint& count) 
	{
	  (void)deviceType;
	  count = sizeof(devices)/sizeof(devices[0]);
	  return PCMIF_OK;
	}
  
	virtual int GetDevices(int deviceType, Sid::List_String& guid, Sid::List_String& p_name, Sid::List_String& productID) 
	{
	  SID_INFO("GetDevices: %s", deviceNames[deviceType]);

	  for(uint i = 0; i < sizeof(devices)/sizeof(devices[0]); ++i)
	  {
	    guid.append(devices[i].guid);
	    p_name.append(devices[i].name);
	    productID.append(devices[i].productID);
	  }

	  return PCMIF_OK;
	}
	
	virtual int GetVolumeParameters(int deviceType, unsigned int &range_min, unsigned int &range_max, unsigned int &volume, int &boost)
	{
	  range_min = 0; 
	  range_max = 100;
	  SID_INFO("GetVolumeParameters: %s", deviceNames[deviceType]);
	  if(deviceType == INPUT_DEVICE)
	    volume = input_volume;
	  else if (deviceType == OUTPUT_DEVICE)
	    volume = output_volume;
	  
	  // input boost is not supported in this example
	  boost = -1;
	  return PCMIF_OK;
	}
  
	virtual int SetVolume(int deviceType, unsigned int volume)
	{
	  SID_INFO("SetVolume: %s, %d", deviceNames[deviceType], volume);
	  if(deviceType == INPUT_DEVICE)
	    input_volume = volume;
	  else if (deviceType == OUTPUT_DEVICE)
	    output_volume = volume;
	  return PCMIF_OK;
	}
  
	virtual int SetInputBoost(int boost)
	{
	  SID_INFO("SetInputBoost: %d", boost);
	  return PCMIF_ERROR_PROP_NOT_SUPPORTED;
	}

	virtual int GetMute(int deviceType, int &muted)
	{
	  SID_INFO("GetMute: %s", deviceNames[deviceType]);
	  if(deviceType == INPUT_DEVICE)
	    muted = input_muted;
	  else if (deviceType == OUTPUT_DEVICE)
	    muted = output_muted;
	  return PCMIF_OK; 
	}

	virtual int SetMute(int deviceType, int mute)
	{
	  SID_INFO("SetMute: %s, %d", deviceNames[deviceType], mute);
	  if(deviceType == INPUT_DEVICE)
	    input_muted = mute;
	  else if (deviceType == OUTPUT_DEVICE)
	    output_muted = mute;
	  return PCMIF_OK;
	}
	
	virtual int GetSampleRateCount(int deviceType, uint& count)
	{
	  SID_INFO("GetSampleRateCount: %s", deviceNames[deviceType]);
	  count = 1;
	  return PCMIF_OK;
	}

	virtual int GetSupportedSampleRates(int deviceType, Sid::List_uint& sampleRates)
	{
	  SID_INFO("GetSupportedSampleRates: %s", deviceNames[deviceType]);
	  if(forceSampleRate)
	    sampleRates.append(forceSampleRate);
	  else
	    sampleRates.append(SAMPLE_RATE);
	    
	  return PCMIF_OK;
	}

	virtual int GetCurrentSampleRate(int deviceType, unsigned int &sampleRate)
	{
	  if(deviceType == INPUT_DEVICE)
	    sampleRate = input_sampleRate;
	  else if (deviceType == OUTPUT_DEVICE)
	    sampleRate = output_sampleRate;
	  else if (deviceType == NOTIFICATION_DEVICE)
	    sampleRate = notification_sampleRate;
	  else
	    return PCMIF_ERROR_UNKNOWN_DEVICE;
	  SID_INFO("GetCurrentSampleRate: %s, %d", deviceNames[deviceType], sampleRate);
	  return PCMIF_OK;
	}

	virtual int SetSampleRate(int deviceType, unsigned int sampleRate)
	{
	  SID_INFO("SetSampleRate: %s, %d", deviceNames[deviceType], sampleRate);

	  if(forceSampleRate > 0 && (uint) forceSampleRate != sampleRate)
	  {
	    return PCMIF_ERROR;
	  }

	  if(deviceType == INPUT_DEVICE)
	    input_sampleRate = sampleRate;
	  else if (deviceType == OUTPUT_DEVICE)
	    output_sampleRate = sampleRate;
	  else if (deviceType == NOTIFICATION_DEVICE)
	    notification_sampleRate = sampleRate;
	  else
	    return PCMIF_ERROR_UNKNOWN_DEVICE;
	  return PCMIF_OK;
	}
	
	virtual int SetNumberOfChannels(int deviceType, int numberOfChannels)
	{
	  SID_INFO("SetNumberOfChannels: %s, %d", deviceNames[deviceType], numberOfChannels);
	  if(deviceType == INPUT_DEVICE)
	  {
	    if(forceInputChannels > 0 && numberOfChannels != forceInputChannels)
	      return PCMIF_ERROR;
	    input_channels = numberOfChannels;
	  }
	  else if (deviceType == OUTPUT_DEVICE)
	  {
	    if(forceOutputChannels > 0 && numberOfChannels != forceOutputChannels)
	      return PCMIF_ERROR;
	    output_channels = numberOfChannels;
	  }
	  else
	    return PCMIF_ERROR_PROP_NOT_SUPPORTED;
	  return PCMIF_OK;
	}

	virtual int CustomCommand(const Sid::String& command, Sid::String& response)
	{
	  SID_INFO("CustomCommand %s", (char*)command.data());
	  if(command == "PING")
	    response = "PONG";
	  else
	    response = "NOT SUPPORTED";
	  return PCMIF_OK;
	}

	void Run()
	{
	  const int sleep_interval = 10;
	  unsigned int stream_time;
	  stream_time = get_milliseconds();
	  int inactive = 1;

	  while(1)
	  {
	    if(Stopping)
	    {
	      return;
	    }
	    if(input_started || output_started)
	      inactive = 0;
	    else if(!inactive && !input_started && !output_started)
	    {
	      loopback_buf.Clear();
	      inactive = 1;
	    }

	    
	    unsigned int current_time = get_milliseconds();
	    if(stream_time > current_time)
	    {
	      Sleep(stream_time - current_time);
	      continue;
	    }

	    stream_time += sleep_interval;

	    input_buf.m_sampleRate = input_sampleRate;
	    output_buf.m_sampleRate = output_sampleRate;

	    input_buf.m_channels = input_channels;
	    output_buf.m_channels = output_channels;
	    
	    #ifdef USE_SINGLE_CALLBACK
	    if(input_started || output_started)
	    {
	      if(input_started)
	      {
		loopback_buf.Pull(input_buf);
 		m_transport->InputAndOutputDeviceReady(sleep_interval, input_buf.m_sampleRate, output_buf.m_sampleRate, input_buf.m_channels, output_buf.m_channels, input_buf.m_data, output_buf.m_data);
	      }
	      else
		m_transport->InputAndOutputDeviceReady(sleep_interval, input_buf.m_sampleRate, output_buf.m_sampleRate, input_buf.m_channels, output_buf.m_channels, null_buf, output_buf.m_data);
	    }
	    #else

	    if(output_started)
	      m_transport->OutputDeviceReady(output_buf.m_sampleRate/ 100,output_buf.m_sampleRate,output_buf.m_channels,output_buf.m_data);
	    if(input_started)
	    {
	      loopback_buf.Pull(input_buf);
	      m_transport->InputDeviceReady(input_buf.m_sampleRate/ 100,input_buf.m_sampleRate,input_buf.m_channels, input_buf.m_data);
	    }
	    #endif
	    if(!output_started)
	      output_buf.Reset();
	    loopback_buf.Push(output_buf);
	    
	  }
	}

	private:
	  int input_started, output_started, notification_started;
	  int input_muted, output_muted;
	  int input_volume, output_volume;
	  volatile int input_sampleRate, output_sampleRate, notification_sampleRate;
	  volatile int output_channels, input_channels;
	  int forceInputChannels, forceOutputChannels, forceSampleRate;
	  AudioFrame input_buf;
	  AudioFrame output_buf;
	  LoopbackBuffer loopback_buf;
	  Sid::Binary null_buf;
	  SkypePCMCallbackInterface* m_transport;
	public:	
	  volatile bool Stopping;
	};

SkypePCMInterface* SkypePCMInterfaceGet(SkypePCMCallbackInterface* transport)
{
  return new PCMLoopback(transport, &gParser);
}
void SkypePCMInterfaceRelease(SkypePCMInterface* pcmif)
{
  PCMLoopback* pcmloopback = (PCMLoopback*) pcmif;
  pcmloopback->Stopping = 1;
  pcmloopback->stop();
  delete pcmif;
}

