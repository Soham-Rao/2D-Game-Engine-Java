//graphics library shader language

//not part of code
//vertex shader
#type vertex
#version 460 core

/*
prefix a = attribute
prefix f = pass to fragment shader
*/
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 aColor;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;

void main(){
    fColor = aColor;
    //needed, apos = 1st 3, 4th = 1
    gl_Position = vec4(aPos, 1.0);
}

//fragment shader
#type fragment
#version 460 core

in vec4 fColor;

out vec4 color;

void main(){
    color = fColor;
}



