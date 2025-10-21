package luckytntlib.client.gui.widget;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

/**
 * Renders a vertically center-aligned String in a Layout.
 * Is used in the config Screen
 */
public class CenteredStringWidget extends TextWidget {

	public CenteredStringWidget(Text component, TextRenderer font) {
		super(component, font);
	}

	@Override
	public void renderWidget(DrawContext graphics, int i1, int i2, float f) {
		// Note: In 1.21.10, DrawContext.getMatrices() returns Matrix3x2fStack
		// which doesn't have push/pop methods. We'll render without the offset for now.
		// TODO: Find the correct way to offset rendering in 1.21.10
		super.renderWidget(graphics, i1, i2 + 6, f);
	}
}