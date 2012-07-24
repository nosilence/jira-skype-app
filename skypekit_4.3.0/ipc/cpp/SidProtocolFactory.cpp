#include "SidProtocolFactory.hpp"

namespace Sid {

  Protocol::ServerInterface* ProtocolFactory::create(const String& protocol, TransportInterface* transport, Field* descriptors)
  {
    if (M_protocol_factory) return M_protocol_factory->create_protocol(protocol, transport, descriptors);
    return 0;
  }

  ProtocolFactory::ProtocolFactory(const String& name, Protocol::ServerInterface* (*constructor)(TransportInterface*, Field*))
  : m_next(M_protocol_factory),
    m_constructor(constructor),
    m_name(name)
  { M_protocol_factory = this;
    // check for duplicate? 
  }

  Protocol::ServerInterface* ProtocolFactory::create_protocol(const String& protocol_name, TransportInterface* transport, Field* descriptors) 
  { 
    if (protocol_name == m_name) {
      return (*m_constructor)(transport, descriptors);
    }
    if (m_next) return m_next->create_protocol(protocol_name, transport, descriptors);
    return 0;
  }

  void ProtocolFactory::use_protocol() { }

  ProtocolFactory* ProtocolFactory::M_protocol_factory;

}
