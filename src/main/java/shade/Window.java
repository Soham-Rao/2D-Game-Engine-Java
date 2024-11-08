package shade;

//imports
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import util.Time;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

//main window class
public class Window {
      private int width, height;
      private String title;
      private long glfwWindow;

      public float r, g, b, a;

      private static Window window = null;

      private static Scene currentScene = null;

      private Window() {
            this.width = 1920;
            this.height = 1080;
            this.title = "Mario";
            r = 1f;
            g = 1f;
            b = 1f;
            a = 1f;
      }

      //changing screens on display
      public static void changeScene(int newScene){
            switch(newScene){
                  case 0:
                        currentScene = new LevelEditorScene();
                        currentScene.init();
                        currentScene.start();
                        break;

                  case 1:
                        currentScene = new LevelScene();
                        currentScene.init();
                        currentScene.start();
                        break;

                  default:
                        assert false: "Unknown Scene ' " + newScene + " '";
                        break;
            }
      }

      //return main window object
      public static Window get() {
            if (Window.window == null) {
                  Window.window = new Window();
            }

            return Window.window;
      }

      //call initialization and event loop
      public void run() {
            System.out.println("Hello from " + Version.getVersion() + "!");

            init();
            loop();

            //Free memory after execution
            glfwFreeCallbacks(glfwWindow);
            glfwDestroyWindow(glfwWindow);

            glfwTerminate();
            Objects.requireNonNull(glfwSetErrorCallback(null)).free();


      }

      //initialization
      public void init() {
            //setup error callback
            GLFWErrorCallback.createPrint(System.err).set();

            //initialize GLFW
            if (!glfwInit()) {
                  throw new IllegalStateException("Unable to initialize GLFW!");
            }

            //Configure GLFW
            glfwDefaultWindowHints();
            glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
            glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
            glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

            //Creating Window
            glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL); //mem adddr
            if (glfwWindow == NULL) {
                  throw new IllegalStateException("Unable to create Window!");
            }

            //forwarding gl callbacks of mouse to our functions
            glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
            glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
            glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
            glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

            //Making the OpenGL Context Current
            glfwMakeContextCurrent(glfwWindow);
            //Enabling Vsync
            glfwSwapInterval(1);

            //Show the window
            glfwShowWindow(glfwWindow);

            /*LWJGL and OpenGL Integration
              Integrates with External Contexts
              LWJGL detects current context in current thread only
             */
            GL.createCapabilities();

            Window.changeScene(0);
      }

      //event loop
      public void loop() {

            float beginTime = Time.getTime();
            float endTime = Time.getTime();
            float dt = -1.0f;

            while (!glfwWindowShouldClose(glfwWindow)) {
                  //I/O Events
                  glfwPollEvents();

                  glClearColor(r, g, b, a);
                  glClear(GL_COLOR_BUFFER_BIT);

                  if(dt >= 0) {
                        currentScene.update(dt);
                  }

                  glfwSwapBuffers(glfwWindow);

                  endTime = Time.getTime();
                  dt = endTime - beginTime;
                  beginTime = endTime;
            }
      }
}
