package renderer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {

      private int shaderProgramID;

      private String vertexSource;
      private String fragmentSource;
      private String filepath;

      public Shader(String filepath){
            //Open file with shader program
            this.filepath = filepath;
            try{
                  String source = new String(Files.readAllBytes(Paths.get(filepath)));
                  String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

                  //regex to find #type 1
                  int index = source.indexOf("#type") + 6;
                  int eol = source.indexOf("\r\n", index);
                  String firstPattern = source.substring(index, eol).trim();

                  //regex to find #type 2
                  index = source.indexOf("#type", eol) + 6;
                  eol = source.indexOf("\r\n", index);
                  String secondPattern = source.substring(index, eol).trim();

                  if(firstPattern.equals("vertex")){
                        vertexSource = splitString[1];
                  } else if (firstPattern.equals("fragment")) {
                        fragmentSource = splitString[1];
                  } else{
                        throw new IOException("Unexpected Token '" + firstPattern + "'");
                  }

                  if(secondPattern.equals("vertex")){
                        vertexSource = splitString[2];
                  } else if (secondPattern.equals("fragment")) {
                        fragmentSource = splitString[2];
                  } else{
                        throw new IOException("Unexpected Token '" + secondPattern + "'");
                  }

            }catch(IOException e){
                  e.printStackTrace();
                  assert false: "Error: Could not open file for shader '" + filepath + "'";
            }

      }

      public void compile(){
            //Compile and Link Shaders

            int vertexID, fragmentID;


            //first load and compile vertex shader
            vertexID = glCreateShader(GL_VERTEX_SHADER);
            //pass shader source code to GPU
            glShaderSource(vertexID, vertexSource);
            glCompileShader(vertexID);

            //check for errors during compilation
            int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
            if(success == GL_FALSE){
                  int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
                  System.out.println("Error: " + filepath + "\n\tVertex shader compilation failed");
                  System.out.println(glGetShaderInfoLog(vertexID, len));
                  assert false: "";
            }


            //next load and compile fragment shader
            fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
            //pass shader source code to GPU
            glShaderSource(fragmentID, fragmentSource);
            glCompileShader(fragmentID);

            //check for errors during compilation
            success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
            if(success == GL_FALSE){
                  int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
                  System.out.println("Error: "+ filepath + "\n\tFragment shader compilation failed");
                  System.out.println(glGetShaderInfoLog(fragmentID, len));
                  assert false: "";
            }


            //Linking shaders
            shaderProgramID = glCreateProgram();
            glAttachShader(shaderProgramID, vertexID);
            glAttachShader(shaderProgramID, fragmentID);
            glLinkProgram(shaderProgramID);

            success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
            if(success == GL_FALSE){
                  int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
                  System.out.println("Error: " + filepath + "\n\tLinking shader compilation failed");
                  System.out.println(glGetProgramInfoLog(shaderProgramID, len));
                  assert false: "";
            }


      }

      public void use(){
            //bind shader program
            glUseProgram(shaderProgramID);

      }

      public void detach(){
            glUseProgram(0);
      }

      public void uploadMat4f(String varName, Matrix4f mat4){
            int varLocation = glGetUniformLocation(shaderProgramID, varName);
            FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
            mat4.get(matBuffer);
            glUniformMatrix4fv(varLocation, false, matBuffer);
      }

}
