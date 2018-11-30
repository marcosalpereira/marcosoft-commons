 package org.marcosoft.lib;


 public class Version implements Comparable<Version>
 {
	 private final String version;


	 public final String get() {
		 return this.version;
		 }


	 public Version(String version) {
		 if (version == null) throw new IllegalArgumentException("Version can not be null");
		 if (!version.matches("[0-9]+(\\.[0-9]+)*"))
			 throw new IllegalArgumentException("Invalid version format:" + version);
		 this.version = version;
		 }


	 @Override
    public boolean equals(Object that)
	 {
		 if (this == that) return true;
		 if (that == null) return false;
		 if (getClass() != that.getClass()) return false;
		 return compareTo((Version) that) == 0;
		 }


	 public int compareTo(Version that) {
		 if (that == null) return 1;
		 final String[] thisParts = get().split("\\.");
		 final String[] thatParts = that.get().split("\\.");
		 final int length = Math.max(thisParts.length, thatParts.length);
		 for (int i = 0; i < length; i++) {
			 final int thisPart = i < thisParts.length ? Integer.parseInt(thisParts[i]) : 0;

			 final int thatPart = i < thatParts.length ? Integer.parseInt(thatParts[i]) : 0;

			 if (thisPart < thatPart) return -1;
			 if (thisPart > thatPart) return 1;
			 }
		 return 0;
		 }


	 @Override
    public String toString()
	 {
		 return this.version;
		 }


	 public boolean gt(Version that) {
		 return compareTo(that) > 0;
		 }


	 public boolean lt(Version that) {
		return compareTo(that) < 0;
	}
	 }
