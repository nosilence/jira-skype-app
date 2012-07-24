
#include "skype-string-list.h"
#include <stdio.h>

/** \cond INTERNAL */
class SEStringList::Element
{
public:
        Element();

public:
        SEString str;
        Element* next;
};

class SEStringList::Data
{
public:
        unsigned int ref;
        Element* list;
        Element* last;
        bool dirty;
        Element** quick_list;
        size_t size;
        SEMutex lock;
};

SEStringList::Element::Element()
                :next(0)
{

}
/** \endcond */

SEStringList::SEStringList()
                :d(0)
{

}

SEStringList::SEStringList(const SEStringList& sl)
                :d(0)
{
        *this = sl;
}

SEStringList::~SEStringList()
{
        d_unref();
}

/** \cond INTERNAL */
void SEStringList::init()
{
        if (d)
                return;

        d = new Data();
        d->ref = 1;
        d->list = 0;
        d->dirty = true;
        d->size = 0;
        d->quick_list = 0;
}
/** \endcond */

SEStringList& SEStringList::append(const SEString& str)
{
        if (str.isNull()) {
                return append("")/**this*/;
        }
        
        detach();

        if (0 == d) {
                init();

                d->list = new Element();
                d->list->str = str;
                d->last = d->list;
        } else {
                d->dirty = true;
                d->last->next = new Element();
                d->last->next->str = str;
                d->last = d->last->next;
        }

        return *this;
}

#if 0
SEStringList& SEStringList::prepend(const SEString& str)
{
        if (str.isNull())
                return *this;

        detach();

        if (0 == d) {
                init();

                d->list = new Element();
                d->list->str = str;
                d->last = d->list;
        } else {
                d->dirty = true;

                Element* n = new Element();

                n->str = str;
                n->next = d->list;
                d->list = n;
        }

        return *this;
}
#endif

SEString SEStringList::peek()
{
        detach();

        if (0 == d)
                return SEString();

        d->dirty = true;

        SEString ret = d->list->str;

        Element* next = d->list->next;

        delete d->list;

        d->list = next;

        if (0 == d->list) {
                delete d;
                d = 0;
        }

        return ret;
}

size_t SEStringList::size() const
{
        if (0 == d)
                return 0;

        // .bwc. Make sure we never have two threads trying to build the
        // quick_list at the same time.
        // !bwc! Might be useful to optimize the case where the reference count
        // is one, and not lock in that case. The contract on this class 
        // forbids having multiple threads touching the same SEString object at
        // the same time, and as long as this holds, the reference count cannot
        // go from 1 to greater than 1 until size() returns.
        SEMutexLock lock_string(d->lock);

        if (!d->dirty)
                return d->size;

        if (d->quick_list)
                delete[] d->quick_list;

        // first pass - count elements
        d->size = 0;

        Element* e = d->list;

        do {
                d->size++;
                e = e->next;
        } while (e);

        // second pass - fill quick access vector
        d->quick_list = new Element*[d->size];

        e = d->list;
        size_t n = 0;

        while (e) {
                d->quick_list[n++] = e;

                e = e->next;
        }

        d->dirty = false;

        return d->size;
}

const SEString &SEStringList::operator[](size_t n) const
{
        if ((0 == d) || (n >= size()))
                return empty;

        return d->quick_list[n]->str;
}

SEString SEStringList::operator[](size_t n)
{
        if ((0 == d) || (n >= size()))
                return SEString();

        // we have already called size() so we are supposed to be "clean"

        return d->quick_list[n]->str;
}

SEStringList& SEStringList::operator=(const SEStringList& sl)
{
        const_cast<SEStringList&>(sl).d_ref();
        d_unref();

        d = sl.d;


        return *this;
}

/** \cond INTERNAL */
void SEStringList::d_ref()
{
        if (0 == d)
                return;
        
        d->lock.Acquire();
        
        d->ref++;
        
        d->lock.Release();
}

void SEStringList::d_unref()
{
        if (0 == d)
                return;
        
        SEMutex* lock = &d->lock;
        
        lock->Acquire();         
        
        if (d->ref > 1)
                d->ref--;
        else {
                Data* tmp_d = d;
                d = 0;
                lock->Release();        
                
                Element* tmp = tmp_d->list;

                while (tmp) {
                        Element* next = tmp->next;

                        delete tmp;

                        tmp = next;
                }

                if (tmp_d->quick_list)
                        delete[] tmp_d->quick_list;

                delete tmp_d;                                
                return; 
        }
        
        lock->Release(); 
}

void SEStringList::detach()
{
        if (0 == d)
                return;
        
        d->lock.Acquire();
        
        if (1 != d->ref) {

                Data* d_new = new Data();
        
                d_new->ref = 1;
                d_new->dirty = true;
                d_new->quick_list = 0;
        
                d_new->list = new Element;
                d_new->list->str = d->list->str;
        
                Element* from = d->list->next;
                Element* to = d_new->list;
        
                while (from) {
                        to->next = new Element;
                        to->next->str = from->str;
        
                        to = to->next;
                        from = from->next;
                }
        
                d_new->last = to;
                
                d->lock.Release();
                
                d_unref();
        
                d = d_new;
                
                return;
        }
        
        d->lock.Release();
}
/** \endcond */

SEString SEStringList::join(const SEString& sep, bool escape_args) const
{
        SEString ret;

        if (!size())
                return ret;

        ret += escape_args ? operator[](0).escape() : operator[](0);

        for (size_t n = 1; n < size(); n++)
                ret += sep + (escape_args ? operator[](n).escape() : operator[](n));

        return ret;
}

SEStringList SEStringList::split(const SEString& str, char sep, char esc)
{
        SEStringList ret;
        size_t from = 0;
        size_t cur = 0;
        const size_t len = str.length();

        if (len == 0)
                return ret;

        // skip leading separators
        // .bwc. Repeated code, but easier to understand this way I think.
        // The compiler may optimize this away.
        while (str[cur] == sep) {
                cur++;
        }

        // .bwc. At this point, cur is not pointing at a separator;
        // we will not end up in the code block that appends below
        // until we have incremented at least once.
        from = cur;

        bool in_escape=false;
        bool in_quotes=false;

        while (cur < len) {
                if (str[cur] == sep) {
                        if (!in_escape && !in_quotes) {

                                ret.append(str.substr(from, cur - 1));

                                while (str[cur] == sep)
                                        cur++;

                                // Not pointing at a separator; will not append
                                // again until we have incremented at least 
                                // once.
                                from = cur;

                                continue;
                        }
                } else if (esc && str[cur] == '\"' && !in_escape) {
                        // !bwc! We check esc because that is what we've always
                        // done. Once I hear back from the guys that 
                        // implemented this about whether this is the intended
                        // behavior, this component might be removed.
                        in_quotes = !in_quotes;
                }

                in_escape = esc && str[cur] == esc && !in_escape;
                cur++;
        }

        // .bwc. cur == from can happen when we have a trailing separator.
        // (both are pointing at the null terminator in this case)
        if(cur != from)
        {
                ret.append(str.substr(from, cur));
        }

        return ret;
}


void SEStringList::resize(const unsigned int new_size)
{
        if (size() == new_size) return;   
        if (new_size == 0) { 
                clear();
                return;     
        }       
        
        detach();      
        unsigned int cursize = size(); //updates quick_list
        
        if (cursize > new_size) { //shrink
                
                Element* elem_last = d->quick_list[new_size-1]; 
                elem_last->next = 0;
                d->last = elem_last;                
                
                for (unsigned int n = new_size; n < cursize; n++) {
                        delete d->quick_list[n];
                }                
                                                
        } else {   //grow
        
                while (cursize < new_size) {
                        if (0 == d) {
                                init();
                                d->list = new Element();
                                d->list->str = "";
                                d->last = d->list;
                        } else {
                                d->last->next = new Element();
                                d->last->next->str = "";
                                d->last = d->last->next;
                        }
                        
                        cursize++;
                }
        }
        
        d->dirty = true;
}

int SEStringList::find_pos(const SEString& val)
{
        int len = size();
        for (int n = 0; n < len; n++) {
                if (d->quick_list[n]->str == val) {
                        return n;
                }
        }
        
        return -1;
}

bool SEStringList::contains(const SEString& val)
{
        return (find_pos(val) >= 0);
}

bool SEStringList::remove_val(const SEString& val)
{
        int pos = find_pos(val);
        if (pos >= 0) return remove_pos(pos);

        return false;
}

bool SEStringList::remove_pos(const unsigned int pos)
{          
        if (0 == d || pos >= size()) return false; 
                                
        detach();         
        unsigned int cursize = size();//updates quick_list         
        
        Element* elem_del = d->quick_list[pos];         
        Element* elem_next = elem_del->next; 
        Element* elem_prev = pos == 0 ? 0 : d->quick_list[pos-1]; 
        
        if (pos == 0) {//first
                delete elem_del;
                d->list = elem_next;               
        } else if (pos == cursize -1) { //last
                delete elem_del;
                elem_prev->next = 0;
                d->last = elem_prev;                
        } else {
                delete elem_del;
                elem_prev->next = elem_next;        
        }         
                 
        d->dirty = true; 
        if (0 == d->list) {
                delete d;
                d = 0;
        }                 

        return true;
}

void SEStringList::clear()
{
        if (0 == d) {
                return;
        }
        
        detach();

        for (unsigned int n = 0; n < size(); n++) {
                delete d->quick_list[n]; 
        }
               
        if (d->quick_list)
                delete[] d->quick_list;

        delete d;
        
        d = 0;        
}

