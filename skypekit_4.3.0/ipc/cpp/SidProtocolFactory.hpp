#ifndef Factories_HPP_INCLUDED
#define Factories_HPP_INCLUDED 

#include "SidPlatform.hpp"
#include "SidProtocolServerInterface.hpp"

namespace Sid {

  struct TransportInterface;
  struct Field;

  class ProtocolFactory {
  public:
    static Protocol::ServerInterface* create(const String& protocol, TransportInterface* transport, Field* descriptors);
    ProtocolFactory(const String& name, Protocol::ServerInterface* (*constructor)(TransportInterface*, Field*));
    void use_protocol();
  private:
    Protocol::ServerInterface* create_protocol(const String& protocol_name, TransportInterface* transport, Field* descriptors);
    ProtocolFactory* m_next;
    Protocol::ServerInterface* (*m_constructor)(TransportInterface*, Field*);
    String m_name;
    static ProtocolFactory* M_protocol_factory;
  };

  template<class ProtocolImpl> class ProtocolRegistration : public ProtocolFactory {
  public:
    ProtocolRegistration(const String& name) 
    : ProtocolFactory(name, &constructor)
    { }
  private:
    static Protocol::ServerInterface* constructor(TransportInterface* transport, Field* fields)
    { return new ProtocolImpl(0, transport, fields); }
  };
}

#endif
