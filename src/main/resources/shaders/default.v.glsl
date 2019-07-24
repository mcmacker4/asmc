#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 normal;
layout (location = 2) in vec2 uvcoords;

out vec2 _uvcoords;
out float _bright;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

const vec3 sundir = normalize(vec3(0.6, 1, 0.2));

void main() {
    vec4 worldPos = modelMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * viewMatrix * worldPos;
    
    _uvcoords = uvcoords;
    _bright = mix(0.55, 1.0, dot(sundir, normal));
}
