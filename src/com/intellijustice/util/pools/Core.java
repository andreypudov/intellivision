/*
 * IntelliJustice Intelligent Referee Assistant System
 *
 * The MIT License
 *
 * Copyright 2011-2014 Andrey Pudov.
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

package com.intellijustice.util.pools;

import com.intellijustice.core.DefaultDataProvider;
import com.intellijustice.core.ExcelDataProvider;
import com.intellijustice.core.SQLDataProvider;
import com.intellijustice.ui.modules.HelpModule;
import com.intellijustice.util.ConsoleFormatter;
import com.intellijustice.util.StatusCodes;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.XMLFormatter;

import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * The general application pool.
 *
 * @author    Andrey Pudov        <mail@andreypudov.com>
 * @version   0.00.00
 * %name      Core.java
 * %date      08:40:00 PM, Oct 12, 2012
 */
public class Core {

    private static final java.util.logging.Logger LOG
            = java.util.logging.Logger.getLogger(
            com.intellijustice.core.Manifest.NAME);

    /* the list of application properties */
    private static final Settings SETTINGS = Settings.getSettings();

    /* the database server connection layer */
    //private static final Server SERVER = Server.getDatabaseServer();

    /* the task execution pool */
    private static final Executor EXECUTOR = Executor.getExecutor();

    /* application synchronization pool */
    private static final Reviser REVISER = Reviser.getReviser();

    /* the common application talk interface */
    //private static final Network NETWORK = Network.getNetwork();
    
    /* the scoreboard window to show competition results */
    private static final Scoreboard SCOREBOARD = Scoreboard.getScoreboard();

    /* the data provider instance */
    private static final DefaultDataProvider dataProvider;

    /* the primary stage for the application */
    private static Stage primaryStage;

    /* the primary scene for the stage */
    private static Scene primaryScene;

    /* the primary panel for the scene */
    private static Node  primaryPanel;

    /* window maximizing stuff */
    private static Rectangle2D windowBounds = null;
    private static boolean     maximized    = false;

     /* transparent feature support property */
    private static final boolean transparent = Platform.isSupported(
            ConditionalFeature.TRANSPARENT_WINDOW);
    private static double primarySceneAnchor = transparent ? 32.0 : 0.0;
    private static String primarySceneStyle  = transparent ? ""
                                                           : "-fx-effect: null";
    static {
        try {
            /* Initialize general application logging */

            /*
             * "/"  the local path name separator
             * "%t" the system temporary directory
             * "%h" the value of the "user.home" system property
             * "%g" the generation number to distinguish rotated logs
             * "%u" a unique number to resolve conflicts
             * "%%" translates to a single percent sign "%"
             */

            final DateFormat formatter = new SimpleDateFormat(
                    "HH-mm-ss dd-MMM-yyyy");

            /* the hostname of the machine */
            final InetAddress localMachine = InetAddress.getLocalHost();

            /* the pattern for naming the output file */
           final String pattern = "%t/intellijustice."
                   + System.getProperty("user.name")  + "."
                   + localMachine.getHostName()       + "."
                   + System.getProperty("os.name")    + "."
                   + System.getProperty("os.version") + "."
                   + System.getProperty("os.arch")    + "."
                   + formatter.format(new Date())     + "."
                   + "%g.log";

            /* specify append mode to disabled */
            final boolean append = false;

            final ConsoleHandler ttyHandler  = new ConsoleHandler();
            final FileHandler    fileHandler = new FileHandler(pattern, append);

            ttyHandler.setFormatter(new ConsoleFormatter());
            fileHandler.setFormatter(new XMLFormatter());

            LOG.addHandler(ttyHandler);
            LOG.addHandler(fileHandler);

            LOG.setUseParentHandlers(false);
        } catch (IOException e) {
            LOG.severe(e.getMessage());
            System.exit(StatusCodes.EXIT_FAILURE);
        }

        LOG.info("IntelliJustice Intelligent Referee Assistant System\n"
                + "Copyright (C) 2011-2014 Andrey Pudov. "
                + "All rights reserved.\n");

        /* adds first-level module to the application window */
        Modules.addModule(HelpModule.getInstance());

        /* running data provider */
        final String provider = SETTINGS.getValue("intellijustice.provider");
        switch(provider) {
            case "Excel":
                /* use Excel data provider as a primary data provider */
                final File worksheet = new File(SETTINGS.getValue(
                        "intellijustice.provider.excel.document"));

                dataProvider = new ExcelDataProvider(worksheet);
                break;
            default:
                /* use MySQL data provider as a prmary data provider */
                dataProvider = new SQLDataProvider();
                break;
        }
    }

    /* do not let anyone instantiate this class */
    private Core() {
    }

    /**
     * Returns the primary stage of the application.
     *
     * @return the primary stage of the application.
     */
    public static synchronized Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Initialize the primary stage of the application with user settings.
     *
     * @param stage the primary stage of the application.
     * @param scene specify the scene to be used on this stage.
     *
     * @throws IOException
     *        if an I/O exception occurs.
     * @throws IllegalArgumentException
     *        method has been passed an illegal or inappropriate argument
     */
    public static synchronized void setPrimaryStage(final Stage stage,
            final Scene scene) throws IOException {
        if ((stage == null) || (scene == null)) {
            throw new IllegalArgumentException();
        }

        primaryScene = scene;
        primaryPanel = scene.lookup("#mainPanel");

        primaryStage = stage;
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("IntelliJustice");
        primaryStage.getIcons().add(new Image(Core.class.getResource(
                "/com/intellijustice/resources/images/IntelliJustice.png"
            ).openStream()));
        primaryStage.setScene(scene);

        /* set window size */
        primaryStage.setWidth(
                Double.parseDouble(SETTINGS.getValue(
                "intellijustice.window.width")));
        primaryStage.setHeight(
                Double.parseDouble(SETTINGS.getValue(
                "intellijustice.window.height")));

        primaryStage.fullScreenProperty().addListener(
            (final ObservableValue<? extends Boolean> ov, final Boolean t, final Boolean t1) -> {
                if (ov.getValue()) {
                    AnchorPane.setBottomAnchor(primaryPanel, 0.0);
                    AnchorPane.setLeftAnchor(primaryPanel,   0.0);
                    AnchorPane.setRightAnchor(primaryPanel,  0.0);
                    AnchorPane.setTopAnchor(primaryPanel,    0.0);

                    primaryScene.getRoot().setStyle("-fx-effect: null");
                } else {
                    AnchorPane.setBottomAnchor(primaryPanel, primarySceneAnchor);
                    AnchorPane.setLeftAnchor(primaryPanel,   primarySceneAnchor);
                    AnchorPane.setRightAnchor(primaryPanel,  primarySceneAnchor);
                    AnchorPane.setTopAnchor(primaryPanel,    primarySceneAnchor);

                    primaryScene.getRoot().setStyle(primarySceneStyle);
                }
        });

        primaryStage.setOnCloseRequest((final WindowEvent event) -> {
            closeWindow();
            
            event.consume();
        });
    }

    /**
     * Closes application window.
     */
    public static synchronized void closeWindow() {
        SETTINGS.setValue("intellijustice.window.width",
                 Double.toString(primaryStage.getWidth()));
        SETTINGS.setValue("intellijustice.window.height",
                Double.toString(primaryStage.getHeight()));

        /* close window while processing application tasks */
        primaryStage.close();

        SCOREBOARD.close();
        EXECUTOR.shutdown();
        // TODO SERVER.disconnect();
        SETTINGS.save();
    }

    /**
     * Returns the maximized state of the application window.
     *
     * @return true if window is maximized and false otherwise.
     */
    public static synchronized boolean isMaximized() {
        return maximized || primaryStage.isFullScreen();
    }

    /**
     * Maximizes application window.
     */
    public static synchronized void maximizeWindow() {
        final Screen screen  = Screen.getScreensForRectangle(
                primaryStage.getX(),
                primaryStage.getY(), 1, 1).get(0);

        if (maximized) {
            maximized = false;

            if (windowBounds != null) {
                primaryStage.setX(windowBounds.getMinX());
                primaryStage.setY(windowBounds.getMinY());
                primaryStage.setWidth(windowBounds.getWidth());
                primaryStage.setHeight(windowBounds.getHeight());
            }

            primaryScene.getRoot().setStyle(primarySceneStyle);

            AnchorPane.setBottomAnchor(primaryPanel, primarySceneAnchor);
            AnchorPane.setLeftAnchor(primaryPanel,   primarySceneAnchor);
            AnchorPane.setRightAnchor(primaryPanel,  primarySceneAnchor);
            AnchorPane.setTopAnchor(primaryPanel,    primarySceneAnchor);
        } else {
            maximized = true;

            windowBounds = new Rectangle2D(
                    primaryStage.getX(),     primaryStage.getY(),
                    primaryStage.getWidth(), primaryStage.getHeight());

            primaryScene.getRoot().setStyle("-fx-effect: null");

            AnchorPane.setBottomAnchor(primaryPanel, 0.0);
            AnchorPane.setLeftAnchor(primaryPanel,   0.0);
            AnchorPane.setRightAnchor(primaryPanel,  0.0);
            AnchorPane.setTopAnchor(primaryPanel,    0.0);

            primaryStage.setX(screen.getVisualBounds().getMinX());
            primaryStage.setY(screen.getVisualBounds().getMinY());
            primaryStage.setWidth(screen.getVisualBounds().getWidth());
            primaryStage.setHeight(screen.getVisualBounds().getHeight());
        }
    }

    /**
     * Sets application window to full screen mode.
     */
    public static synchronized void maximizeWindowToScreen() {
        primaryStage.setFullScreen(!primaryStage.isFullScreen());
    }

    /**
     * Minimizes application window.
     */
    public static synchronized void minimizeWindow() {
        primaryStage.setIconified(true);
    }

    /**
     * Returns selected data provider.
     *
     * @return the data provider used by application settings.
     */
    public static synchronized DefaultDataProvider getDataProvider() {
        return dataProvider;
    }

    /**
     * Return application synchronization pool.
     *
     * @return the application synchronization pool.
     */
    public static synchronized Reviser getReviser() {
        return REVISER;
    }
    
    /**
     * Return scoreboard window instance..
     *
     * @return the scoreboard window instance.
     */
    public static synchronized Scoreboard getScoreboard() {
        return SCOREBOARD;
    }
}
