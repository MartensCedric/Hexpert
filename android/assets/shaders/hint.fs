#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

void main() {
        vec4 tc = texture2D(u_texture, v_texCoords).rgba;
        if(texture2D(u_texture, v_texCoords).a < 0.4)
        {
            discard;
        }
        else if((tc.r + tc.g + tc.b) < 0.35){
            gl_FragColor = vec4(tc.rgb, 1);
        }
        else{
            gl_FragColor = vec4(tc.r + 0.2, tc.g + 0.2, tc.b, 1);
        }
}

