package com.jx.ui.fx;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

public class FXUtil {
	@SuppressWarnings("unchecked")
	static public <T> T loadUI(String path) {
		// try {
		// return (T) FXMLLoader.load(Paths.get(path).toUri().toURL());
		// } catch (IOException e) {
		// // System.out.printf("Ignore: %s %s\n", path,
		// // e.getLocalizedMessage());
		// }
		try {
			URL resource = FXUtil.class.getResource(path);
			if (resource == null) throw new RuntimeException("Cannot find path: " + path);
			return (T) FXMLLoader.load(resource);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Load Node from fxml with given path file
	 *
	 * @param node
	 * @param path
	 */
	public static void loadNodeFXML(Node node, String path) {
		loadNodeFXML(node, node.getClass().getResource(path));
	}

	/**
	 * Load Node from fxml with given path file
	 *
	 * @param node
	 * @param url
	 */
	public static void loadNodeFXML(Node node, URL url) {
		FXMLLoader loader = new FXMLLoader(url);
		loader.setRoot(node);
		loader.setController(node);
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	static public String loadStyleSheet(Class<?> type) {
		String name = type.getSimpleName();
		URL url = type.getResource(name);
		if (url == null) url = type.getResource(name + ".css");
		return url.toExternalForm();
	}

	static public String url(String path) {
		return FXUtil.class.getResource(path).toExternalForm();
	}

	/**
	 * Load image from given absolute path by class path
	 *
	 * @param path
	 * @return Image
	 */
	static public Image loadImage(String path) {
		try (InputStream stream = FXUtil.class.getResourceAsStream(path)) {
			return new Image(stream);
		} catch (IOException ex) {
			// this exception occurs when close method invoked,
			// so it's safe because it will not occur
			throw new RuntimeException(ex);
		}
	}

	static public Font loadFont(String name, int size) {
		try (InputStream in = FXUtil.class.getResourceAsStream("/fonts/" + name)) {
			return Font.loadFont(in, size);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("cannot find font file " + name);
			return new Font(size);
		}
	}
}
