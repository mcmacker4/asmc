#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 uvcoords;

out vec2 _uvcoords;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

void main() {
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(position, 1.0);
    _uvcoords = uvcoords;
}
