package acs.module.record;


import java.io.Serializable;


public class ClickStruct extends Structure implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		String viewId;
		public ClickStruct()
		{
			super(recordType.CLICK);
		}
		
		public ClickStruct(recordType type)
		{
			super(type);
		}
		
		public String getViewId() {
			return viewId;
		}
		public void setViewId(String viewId) {
			this.viewId = viewId;
		}
	}