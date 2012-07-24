#pragma once

/*!
 * COMPILER_BARRIER should guarantee memory ordering for SMP CPUs 
 * even on non-coherent archs like ARMv7 and MIPS.
 */
 
#ifndef COMPILER_BARRIER
#define COMPILER_BARRIER() __asm__ __volatile__("" : : : "memory")
//#error You have to provide your own implementation of COMPILER_BARRIER macro.
#endif
