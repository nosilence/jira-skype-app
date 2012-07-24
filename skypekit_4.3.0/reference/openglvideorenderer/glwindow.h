
#include <VideoTransportClient.hpp>
#include <SysVShm.hpp>

#ifdef __APPLE__
#include <OpenGL/CGLTypes.h>
#include <OpenGL/CGLContext.h>
#include <OpenGL/OpenGL.h>
#include <GLUT/glut.h>
#else
#include <GL/gl.h>
#include <GL/freeglut.h>
#endif

class glWindow
{
	public:
		glWindow( const char *wname );
		~glWindow();

		int getKey();

		static void run_events();

		static void displayFunc( void );
		static void idleFunc( void );

		static bool glutinitialized;

		void displayFrame();
		void queryFrame();

	private:
		VideoTransportClient <SysVShm> ipc;

		static glWindow *first;
		glWindow *next;

		GLuint tex_id;
		int win_id;

		int textureWidth, textureHeight;

};
