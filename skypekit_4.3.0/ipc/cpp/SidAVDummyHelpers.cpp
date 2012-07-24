#include "SidPlatform.hpp"

namespace Sid
{

bool dummy_field_equals_default(const void* p) {(void)p; return 0;}
void dummy_set_field_to_default(void* p) {(void)p;}
void dummy_append (void* list, uint size, void*&elem) {(void)list; (void)size; (void)elem; }
void* dummy_iterate(void* list, uint size) { (void)list; (void)size; return 0; }
void dummy_reserve(void* list, uint size) { (void)list; (void)size; }
uint dummy_begin_message() {return 0; }

}
