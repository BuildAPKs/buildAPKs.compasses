package acs.module.record;

import java.io.Serializable;

import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;

public class CameraStruct extends Structure implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private PictureCallback pcb;
		private Camera camera;
		private String methodName;

		/**
		 * @return the methodName
		 */
		public CameraStruct()
		{
			super(recordType.CAMERA);
		}
		public String getMethodName() {
			return methodName;
		}
		/**
		 * @param methodName the methodName to set
		 */
		public void setMethodName(String methodName) {
			this.methodName = methodName;
		}
		public Camera getCamera() {
			return camera;
		}
		/**
		 * @param camera the camera to set
		 */
		public void setCamera(Camera camera) {
			this.camera = camera;
		}
		/**
		 * @return the picture callback
		 */
		public PictureCallback getPcb() {
			return pcb;
		}
		/**
		 * @param pcb the pcb to set
		 */
		public void setPcb(PictureCallback pcb) {
			this.pcb = pcb;
		}

	}
