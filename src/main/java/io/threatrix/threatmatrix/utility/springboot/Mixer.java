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
	/**
	* https://github.com/spring-projects/spring-boot/blob/v2.4.6/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/audit/InMemoryAuditEventRepository.java
	*/
	public List<AuditEvent> find(String principal, Instant after, String type) {
		LinkedList<AuditEvent> events = new LinkedList<>();
		synchronized (this.monitor) {
			for (int i = 0; i < this.events.length; i++) {
				AuditEvent event = resolveTailEvent(i);
				if (event != null && isMatch(principal, after, type, event)) {
					events.addFirst(event);
				}
			}
		}
		return events;
	}
  
}
