package org.main;

import com.jogamp.opengl.GLAutoDrawable;

public interface OpenGLModel {
    void display(GLAutoDrawable drawable);

    String toString();
}
