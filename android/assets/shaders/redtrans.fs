#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

void main() {
        vec2 color = texture2D(u_texture, v_texCoords).gb;
        if(texture2D(u_texture, v_texCoords).a < 0.4)
        {
            discard;
        }else{
            gl_FragColor = vec4(1.0, color, 0.5);
        }
}