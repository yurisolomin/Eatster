package ru.baccasoft.eatster.image;

public class DisplaySize {
	public int width;
	public int height;
	
	public DisplaySize() {}
	public DisplaySize( int width, int height ) {
		this.width = width;
		this.height = height;
	}
	public DisplaySize( DisplaySize other ) {
		this.width = other.width;
		this.height = other.height;
	}
	
	public DisplaySize rotate() {
		return new DisplaySize( height, width );
	}
	
	/**
	 * Сравнение, некорректное с точки зрения строгой логики, но "достаточное" для целей сравнения экранов
	 */
	public boolean isLargerThan( DisplaySize other ) {
		return this.width > other.width || this.height > other.height;
	}

	/**
	 * Сравнение, некорректное с точки зрения строгой логики, но "достаточное" для целей сравнения экранов
	 */
	public boolean isSmallerThan( DisplaySize other ) {
		return this.width < other.width || this.height < other.height;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + width;
		return result;
	}

	@Override
	public boolean equals( Object obj ) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DisplaySize other = (DisplaySize) obj;
		if (height != other.height)
			return false;
		if (width != other.width)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[ w="+width+", h="+height+" ]";
	}
}