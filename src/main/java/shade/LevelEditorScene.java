package shade;

import static org.lwjgl.opengl.GL20.*;

//level editor class
public class LevelEditorScene extends Scene {

      private String vertexShaderSrc = """
                  #version 460 core

                  layout (location = 0) in vec3 aPos;
                  layout (location = 1) in vec4 aColor;
                  
                  out vec4 fColor;
                  
                  void main(){
                      fColor = aColor;
                      gl_Position = vec4(aPos, 1.0);
                  }
                  """;
      private String fragmenShaderSrc = """
                  #version 460 core
                  
                  in vec4 fColor;
                  
                  out vec4 color;
                  
                  void main(){
                      color = fColor;
                  }
                  """;

      private int vertexID, fragmentID, shaderProgram;

      private float vertexArray[] = {
            //position, color
                   0.5f, -0.5f, 0.0f,      1.0f, 0.0f, 0.0f, 1.0f, //bottom right 0
                  -0.5f,  0.5f, 0.0f,      0.0f, 1.0f, 0.0f, 1.0f, //top left     1
                   0.5f,  0.5f, 0.0f,      0.0f, 0.0f, 1.0f, 1.0f, //top right    2
                  -0.5f, -0.5f, 0.0f,      1.0f, 1.0f, 0.0f, 1.0f, //bottom left  3
      };

      //MUST BE IN counter-clockwise order
      private int elementArray[] = {
            /*
            x           x


            x           x
             */
                  2, 1, 0, //top right triangle
                  0, 1, 3 // bottom left triangle
      };

      public LevelEditorScene(){

      }

      @Override
      public void init(){
            /*
            Compile and link shaders
             */

            //first load and compile vertex shader
            vertexID = glCreateShader(GL_VERTEX_SHADER);
            //pass shader source code to GPU
            glShaderSource(vertexID, vertexShaderSrc);
            glCompileShader(vertexID);

            //check for errors during compilation
            int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
            if(success == GL_FALSE){
                  int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
                  System.out.println("Error: defaultShader.glsl\n\tVertex shader compilation failed");
                  System.out.println(glGetShaderInfoLog(vertexID, len));
                  assert false: "";
            }

            //next load and compile fragment shader
            fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
            //pass shader source code to GPU
            glShaderSource(fragmentID, fragmenShaderSrc);
            glCompileShader(fragmentID);

            //check for errors during compilation
            success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
            if(success == GL_FALSE){
                  int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
                  System.out.println("Error: defaultShader.glsl\n\tFragment shader compilation failed");
                  System.out.println(glGetShaderInfoLog(fragmentID, len));
                  assert false: "";
            }

            //link shaders and check for errors
            shaderProgram = glCreateProgram();
            glAttachShader(shaderProgram, vertexID);
            glAttachShader(shaderProgram, fragmentID);
            glLinkProgram(shaderProgram);

            success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
            if(success == GL_FALSE){
                  int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
                  System.out.println("Error: defaultShader.glsl\n\tLinking shader compilation failed");
                  System.out.println(glGetProgramInfoLog(shaderProgram, len));
                  assert false: "";
            }

            //generate VAO, VBO, EBO, and send to GPU

      }

      @Override
      public void update(float dt){

      }
}
