#version 330 core

layout (location = 0) in vec3 position;

out vec4 color;

uniform mat4 modelMatrix;

void main() {
    gl_Position = modelMatrix * vec4(position, 1.0);
    color = vec4(position + 0.5, 1.0);
}