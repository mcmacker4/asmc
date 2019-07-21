#version 330 core

in vec2 _uvcoords;
in float _bright;

out vec4 FragColor;

uniform sampler2D tex;

void main() {
    FragColor = texture(tex, _uvcoords) * _bright;
}