#ifndef Sid_Api_HPP_INCLUDED
#define Sid_Api_HPP_INCLUDED

#include <SidPlatform.hpp>
#include "SidCommonConfig.hpp"

namespace Sid {
  class Api { public: int placeholder_for_deprecated_class; };
  int* build_property_change_filter(const char* filter_spec, int num_properties, int (*get_property_idx)(uint, uint));  
}

#endif

