#pragma once

#include <pthread.h>

/*!
 * RAII wrapper for pthread mutexes locking.
 * @internal
 */
class MutexLock
{
	pthread_mutex_t& mutex;
	// disable copy
	MutexLock(const MutexLock&);
	MutexLock& operator =(const MutexLock&);
public:
	MutexLock(pthread_mutex_t& mtx) : mutex(mtx) { pthread_mutex_lock(&mutex); }
	~MutexLock() { pthread_mutex_unlock(&mutex); }
};
