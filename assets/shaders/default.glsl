//graphics library shader language
/*
locations change as per vaos
uniforms are loaded in gpu mem
in - input
out - output
every vertex out needs a fragment in as they're related and required
*/


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
layout (location = 2) in vec2 aTextCoords;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fTextCoords;

void main(){
    fColor = aColor;
    fTextCoords = aTextCoords;
    //needed, apos = 1st 3, 4th = 1
    gl_Position = uProjection * uView * vec4(aPos, 1.0);
}

//fragment shader
#type fragment
#version 460 core

uniform float uTime;
uniform sampler2D TEX_SAMPLER;

in vec4 fColor;
in vec2 fTextCoords;

out vec4 color;

void main(){
    color = texture(TEX_SAMPLER, fTextCoords);
}



