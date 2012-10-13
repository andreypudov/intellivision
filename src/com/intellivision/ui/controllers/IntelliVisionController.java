/*
 * IntelliVision Intelligence Image Processing System
 *
 * The MIT License
 *
 * Copyright 2011-2012 Andrey Pudov.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.intellivision.ui.controllers;

import com.intellivision.util.pools.Core;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;

/**
 * FXML Controller class provides window resizing and moving functionality.
 *
 * @author    Andrey Pudov        <mail@andreypudov.com>
 * @version   0.00.00
 * %name      IntelliVisionController.java
 * %date      05:00:00 PM, Aug 24, 2012
 */
public class IntelliVisionController implements Initializable {

    private static final java.util.logging.Logger LOG
            = java.util.logging.Logger.getLogger("IntelliVision");

    /* the type of mouse movement */
    private enum Movement {
        EAST,
        SOUTH_EAST,
        SOUTH
    }

    @FXML private AnchorPane mainPanel;
    @FXML private ToolBar    toolBar;
    @FXML private Label      title;

    private Movement movement = Movement.SOUTH_EAST;

    private double mouseDragOffsetX = 0.0;
    private double mouseDragOffsetY = 0.0;

    private double stageDragOffsetX = 0.0;
    private double stageDragOffsetY = 0.0;

    private boolean windowResizingOn = false;

    /**
     * Initializes the controller class.
     *
     * @param url the location used to resolve relative paths for the root
     *            object, or null if the location is not known.
     * @param rb  the resources used to localize the root object, or null if the
     *            root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    /**
     * Processes shortcuts events and calls assigned methods.
     *
     * @param keyEvent the event source.
     */
    public void anchorPaneKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.F) {
            Core.maximizeWindowToScreen();

            keyEvent.consume();
        }
    }

    /**
     * Resize window when a mouse button is pressed and then dragged.
     *
     * @param event the event source.
     */
    public void anchorPaneMouseDragged(MouseEvent event) {
        double stageOffsetWidth;
        double stageOffsetHeight;

        if(windowResizingOn == true) {
            switch (movement) {
                case EAST:
                    mainPanel.getScene().getWindow().setWidth(
                            event.getScreenX() + stageDragOffsetX);
                    break;
                case SOUTH_EAST:
                    mainPanel.getScene().getWindow().setWidth(
                            event.getScreenX() + stageDragOffsetX);
                    mainPanel.getScene().getWindow().setHeight(
                            event.getScreenY() + stageDragOffsetY);
                    break;
                case SOUTH:
                    mainPanel.getScene().getWindow().setHeight(
                            event.getScreenY() + stageDragOffsetY);
                    break;
                default:
                    break;
            }
        }

        if (mainPanel.getScene().getWindow().getHeight() < 600) {
            mainPanel.getScene().getWindow().setHeight(600);
        }

        if (mainPanel.getScene().getWindow().getWidth() < 800) {
            mainPanel.getScene().getWindow().setWidth(800);
        }

        event.consume();
    }

    /**
     * Changes mouse cursor type when mouse located near window borders.
     *
     * @param event the event source.
     */
    public void anchorPaneMouseMoved(MouseEvent event) {
        /* threshold border size for resize cursor */
        final double THRESHOLD = 8.0;

        final double abcissa   = event.getX();
        final double ordinate  = event.getY();

        final double width     = mainPanel.getWidth();
        final double height    = mainPanel.getHeight();

        final boolean north = (ordinate < THRESHOLD);
        final boolean east  = ((width  - abcissa)  < THRESHOLD);
        final boolean south = ((height - ordinate) < THRESHOLD);
        final boolean west  = (abcissa  < THRESHOLD);

        windowResizingOn = true;

        if (east) {
            if (east && south) {
                mainPanel.setCursor(Cursor.SE_RESIZE);
                movement = Movement.SOUTH_EAST;
            } else {
                mainPanel.setCursor(Cursor.E_RESIZE);
                movement = Movement.EAST;
            }
        } else if (south) {
            mainPanel.setCursor(Cursor.S_RESIZE);
            movement = Movement.SOUTH;
        } else {
            mainPanel.setCursor(Cursor.DEFAULT);
            windowResizingOn = false;
        }

        event.consume();
    }

    /**
     * Sets window offset values before window will be resized.
     *
     * @param event the event source.
     */
    public void anchorPaneMousePressed(MouseEvent event) {
        stageDragOffsetX = mainPanel.getScene().getWidth()
                - event.getScreenX();
        stageDragOffsetY = mainPanel.getScene().getHeight()
                - event.getScreenY();

        /* sets values of mouse offset for W, SW, NW corners */
        mouseDragOffsetX = event.getSceneX();
        mouseDragOffsetY = event.getSceneY();

        event.consume();
    }

    /**
     * Catches mouse events on tool bar panel and maximizes window size when
     * user clicks on tool bar twice at one time.
     *
     * @param event the event source.
     */
    public void toolBarMouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Core.maximizeWindow();
        }

        event.consume();
    }

    /**
     * Sets mouse offset values before window will be moved.
     *
     * @param event the event source.
     */
    public void toolBarMousePressed(MouseEvent event) {
        mouseDragOffsetX = event.getSceneX();
        mouseDragOffsetY = event.getSceneY();
    }

    /**
     * Moves window when a mouse button is pressed and then dragged.
     *
     * @param event the event source.
     */
    public void toolBarMouseDragged(MouseEvent event) {
        if((windowResizingOn == false) && (Core.isMaximized() == false)) {
            mainPanel.getScene().getWindow().setX(
                    event.getScreenX() - mouseDragOffsetX);
            mainPanel.getScene().getWindow().setY(
                    event.getScreenY() - mouseDragOffsetY);
        }
    }
}
