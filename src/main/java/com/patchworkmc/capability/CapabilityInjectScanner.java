package com.patchworkmc.capability;

import java.util.function.Consumer;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

import com.patchworkmc.Patchwork;
import com.patchworkmc.annotation.StringAnnotationHandler;

public class CapabilityInjectScanner extends ClassVisitor {
	private static final String CAPABILITY_INJECT = "Lnet/minecraftforge/common/capabilities/CapabilityInject;";
	private final Consumer<CapabilityInject> consumer;

	public CapabilityInjectScanner(ClassVisitor parent, Consumer<CapabilityInject> consumer) {
		super(Opcodes.ASM7, parent);

		this.consumer = consumer;
	}

	@Override
	public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
		return new FieldScanner(super.visitField(access, name, descriptor, signature, value), access, name, descriptor);
	}

	class FieldScanner extends FieldVisitor {
		private int access;
		private String name;
		private String descriptor;
		private boolean visited;

		FieldScanner(FieldVisitor parent, int access, String name, String descriptor) {
			super(Opcodes.ASM7, parent);

			this.access = access;
			this.name = name;
			this.descriptor = descriptor;
		}

		@Override
		public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
			if (!descriptor.equals(CAPABILITY_INJECT)) {
				return super.visitAnnotation(descriptor, visible);
			}

			if ((access & Opcodes.ACC_STATIC) == 0) {
				Patchwork.LOGGER.error("Field " + name + " marked with an @CapabilityInject annotation was not static! All @CapabilityInject fields must be static.");

				return null;
			}

			return new StringAnnotationHandler(value -> {
				consumer.accept(new CapabilityInject.Field(name, this.descriptor, value));
			});
		}
	}
}
