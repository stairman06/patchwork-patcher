package com.patchworkmc.capability;

public class CapabilityInject {
	protected String capabilityDescriptor;

	private CapabilityInject(String capabilityDescriptor) {
		this.capabilityDescriptor = capabilityDescriptor;
	}

	public String getCapabilityDescriptor() {
		return capabilityDescriptor;
	}

	public static class Field extends CapabilityInject {
		private String field;
		private String fieldDescriptor;

		Field(String field, String fieldDescriptor, String capabilityDescriptor) {
			super(capabilityDescriptor);

			this.field = field;
			this.fieldDescriptor = fieldDescriptor;
		}

		public String getField() {
			return field;
		}

		public String getFieldDescriptor() {
			return fieldDescriptor;
		}

		@Override
		public String toString() {
			return "CapabilityInject.Field{" +
					"field='" + field + '\'' +
					", fieldDescriptor='" + fieldDescriptor + '\'' +
					", capabilityDescriptor='" + capabilityDescriptor + '\'' +
					'}';
		}
	}
}
