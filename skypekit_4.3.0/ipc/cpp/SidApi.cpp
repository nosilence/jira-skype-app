#include "SidApi.hpp"
#include "string.h"
#include "stdio.h"

namespace Sid {

  int* build_property_change_filter(const char* filter_spec, int num_properties, int (*get_property_idx)(uint,uint)) {
    const char* begin = 0;
    int  num_word = (num_properties+sizeof(int)-1)/sizeof(int);
    int* filter = new int[num_word];
    if (filter_spec && (begin = strstr(filter_spec, "SkypeKit/SubscribedProperties=")) != 0) {
      for (int i = num_word; i-- > 0; filter[i] = 0) { } 
      // parse the filter
      uint modid, propid;
      for (const char* i=begin+strlen("SkypeKit/SubscribedProperties"); 
           i && sscanf(++i,"%u:%u",&modid,&propid) == 2; 
           i = strchr(i, ',')) {
        int idx = get_property_idx(modid,propid);
        if (idx >= 0) { 
          filter[idx/sizeof(int)]|=1<<(idx%sizeof(int));
        }
      }
    } else {
      for (int i = num_word; i-- > 0; filter[i] = ~0) { } 
    }
    return filter;
  }
}

