package shade;

//import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
//import static org.lwjgl.opengl.ARBVertexBufferObject.glBindBufferARB;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import renderer.Shader;

import java.nio.*;

//level editor class
public class LevelEditorScene extends Scene {

      private int vertexID, fragmentID, shaderProgram;
      private int vaoID, vboID, eboID;

      private Shader defaultShader;

      private float vertexArray[] = {
                  //position, color
                  0.5f, -0.5f, 0.0f,      1.0f, 1.0f, 0.0f, 1.0f, //bottom right 0
                  -0.5f,  0.5f, 0.0f,      0.0f, 1.0f, 1.0f, 1.0f, //top left     1
                  0.5f,  0.5f, 0.0f,      1.0f, 0.0f, 1.0f, 1.0f, //top right    2
                  -0.5f, -0.5f, 0.0f,      1.0f, 1.0f, 1.0f, 1.0f, //bottom left  3
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
            this.camera = new Camera(new Vector2f());

            defaultShader = new Shader("assets/shaders/default.glsl");
            defaultShader.compile();

            //generate VAO, VBO, EBO, and send to GPU
            vaoID = glGenVertexArrays();
            glBindVertexArray(vaoID);

            //float buffer of vertices
            FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
            vertexBuffer.put(vertexArray).flip();

            //create and upload vbo
            vboID = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

            //create and upload indices
            IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
            elementBuffer.put(elementArray).flip();

            eboID = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

            //add vertex attribute pointers
            int positionsSize = 3;
            int colorSize = 4;
            int floatSizeBytes = 4;
            int vertexSizeBytes = floatSizeBytes * (positionsSize + colorSize);

            glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
            glEnableVertexAttribArray(0);

            glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
            glEnableVertexAttribArray(1);


      }

      @Override
      public void update(float dt){
            defaultShader.use();
            defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
            defaultShader.uploadMat4f("uView", camera.getViewMatrix());
            //bind vao
            glBindVertexArray(vaoID);

            //enable vertex attribute pointers
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);

            glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);


            //unbind all
            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);

            //unbind vao
            glBindVertexArray(0);

            defaultShader.detach();

      }
}