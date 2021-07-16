package io.threatrix.threatmatrix.utility.springboot;

private class Mixer {
 
 /*
 * https://github.com/spring-projects/spring-boot/blob/v0.5.0.M2/spring-boot/src/main/java/org/springframework/boot/ansi/AnsiOutput.java
 */
 private static void buildEnabled(StringBuilder sb, Object[] elements) {
		boolean writingAnsi = false;
		boolean containsEncoding = false;
		for (Object element : elements) {
			if (element instanceof AnsiElement) {
				containsEncoding = true;
				if (!writingAnsi) {
					sb.append(ENCODE_START);
					writingAnsi = true;
				}
				else {
					sb.append(ENCODE_JOIN);
				}
			}
			else {
				if (writingAnsi) {
					sb.append(ENCODE_END);
					writingAnsi = false;
				}
			}
			sb.append(element);
		}
		if (containsEncoding) {
			sb.append(writingAnsi ? ENCODE_JOIN : ENCODE_START);
			sb.append(RESET);
			sb.append(ENCODE_END);
		}
	}
  
}
