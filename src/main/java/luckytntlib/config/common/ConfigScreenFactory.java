package luckytntlib.config.common;

import java.util.function.Supplier;

/**
 * Factory interface for creating config screens.
 * The actual return type should be a Screen but this uses Supplier to avoid client dependencies.
 */
public interface ConfigScreenFactory extends Supplier<Object> {
	// The supplier method from parent interface
}
