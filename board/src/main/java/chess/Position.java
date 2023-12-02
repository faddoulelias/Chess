package chess;

public class Position {
    private int rank;
    private int file;

    public Position(int rank, int file) {
        if (rank < 0 || rank > 7) {
            throw new IllegalArgumentException("x must be between 0 and 7");
        }

        if (file < 0 || file > 7) {
            throw new IllegalArgumentException("y must be between 0 and 7");
        }

        this.rank = rank;
        this.file = file;
    }

    public Position(String position) {
        this.file = position.charAt(0) - 'a';
        this.rank = 8 - (position.charAt(1) - '0');
    }

    /**
     * Returns true if this position is directly in front of the other position for
     * the given color. (for pawns)
     *
     * @param color the color of the piece at this position
     * @param other the other position
     * @return true if this position is directly in front of the other position for
     *         the given color
     */
    public boolean isDirectlyInFrontOf(Position other, PieceColor color) {
        if (color == PieceColor.WHITE) {
            return this.file == other.file && this.rank - other.rank == -1;
        } else {
            return this.file == other.file && this.rank - other.rank == 1;
        }
    }

    /**
     * Returns true if this position is two spaces in front of the other position
     * for the given color. (for pawns on their first move)
     *
     * @param color the color of the piece at this position
     * @param other the other position
     * @return true if this position is two spaces in front of the other position
     *         for the given color
     */
    public boolean isInFrontByTwoOf(Position other, PieceColor color) {
        if (color == PieceColor.WHITE) {
            return this.file == other.file && this.rank - other.rank == -2;
        } else {
            return this.file == other.file && this.rank - other.rank == 2;
        }
    }

    /**
     * Returns true if this position is diagonally in front of the other position
     * for the given color. (for pawns attacking)
     *
     * @param color the color of the piece at this position
     * @param other the other position
     * @return true if this position is diagonally in front of the other position
     *         for the given color
     */
    public boolean isDiagonallyDirectlyInFrontOf(Position other, PieceColor color) {
        if (color == PieceColor.WHITE) {
            return Math.abs(this.file - other.file) == 1 && this.rank - other.rank == -1;
        } else {
            return Math.abs(this.file - other.file) == 1 && this.rank - other.rank == 1;
        }
    }

    /**
     * Returns true if the destionation is L-shaped from this position.
     * (for knights)
     * 
     * @param other the other position
     * @return true if the destionation is L-shaped from this position
     */
    public boolean isLShapedFrom(Position other) {
        return (Math.abs(this.rank - other.rank) == 1 && Math.abs(this.file - other.file) == 2)
                || (Math.abs(this.rank - other.rank) == 2 && Math.abs(this.file - other.file) == 1);
    }

    /**
     * Returns the position in front of this position for the given color. (for
     * pawns)
     * 
     * @param color the color of the piece at this position
     * @return the position in front of this position for the given color
     */
    public Position getPositionInFront(PieceColor color) {
        if (color == PieceColor.WHITE) {
            return new Position(this.rank - 1, this.file);
        } else {
            return new Position(this.rank + 1, this.file);
        }
    }

    /**
     * Returns true if this position is on the same diagonal as the other position.
     * (for bishops and queens)
     * 
     * @note This method does not check if there are any pieces in the way.
     * @param other the other position
     * @return true if this position is on the same diagonal as the other position
     */
    public boolean isOnSameDiagonalAs(Position other) {
        return Math.abs(this.rank - other.rank) == Math.abs(this.file - other.file);
    }

    // ===============================================================================================
    // NOT VALIDATED YET
    // ===============================================================================================

    /**
     * Returns true if this position is directly around the other position. (for
     * kings)
     * 
     * @param other the other position
     * @return true if this position is directly around the other position
     */
    public boolean isDirectlyAround(Position other) {
        return Math.abs(this.rank - other.rank) <= 1 && Math.abs(this.file - other.file) <= 1 && !this.equals(other);
    }

    /**
     * Returns true if this position is on the same file as the other position.
     * (for rooks and queens)
     * 
     * @param other the other position
     * @return true if this position is on the same file as the other position
     */
    public boolean isOnSameFileAs(Position other) {
        return this.rank == other.rank;
    }

    /**
     * Returns true if this position is on the same rank as the other position.
     * (for rooks and queens)
     * 
     * @param other the other position
     * @return true if this position is on the same rank as the other position
     */
    public boolean isOnSameRankAs(Position other) {
        return this.file == other.file;
    }

    /**
     * Returns the positions between this position and the other position. (for
     * rooks and queens)
     * 
     * @param other the other position
     * @return the positions between this position and the other position excluding
     *         the destination
     */
    public Position[] getRankPathTo(Position destination) {
        if (!this.isOnSameRankAs(destination)) {
            throw new IllegalArgumentException("Positions must be on the same rank");
        }

        int direction = this.rank < destination.rank ? 1 : -1;

        Position[] path = new Position[Math.abs(this.rank - destination.rank) - 1];

        int x = this.rank + direction;
        for (int i = 0; i < path.length; i++) {
            path[i] = new Position(x, this.file);
            x += direction;
        }

        return path;
    }

    /**
     * Returns the positions between this position and the other position. (for
     * rooks and queens)
     * 
     * @param other the other position
     * @return the positions between this position and the other position excluding
     *         the destination
     */
    public Position[] getFilePathTo(Position destination) {
        if (!this.isOnSameFileAs(destination)) {
            throw new IllegalArgumentException("Positions must be on the same file");
        }

        int direction = this.file < destination.file ? 1 : -1;

        Position[] path = new Position[Math.abs(this.file - destination.file) - 1];

        int y = this.file + direction;
        for (int i = 0; i < path.length; i++) {
            path[i] = new Position(this.rank, y);
            y += direction;
        }

        return path;
    }

    /**
     * Returns the positions between this position and the other position. (for
     * rooks and queens)
     * 
     * @param other the other position
     * @return the positions between this position and the other position excluding
     *         the destination
     */
    public Position[] getLinearPathTo(Position destination) {
        if (!this.isOnSameFileAs(destination) && !this.isOnSameRankAs(destination)) {
            throw new IllegalArgumentException("Positions must be on the same file or rank");
        }

        if (this.isOnSameFileAs(destination)) {
            return this.getFilePathTo(destination);
        } else {
            return this.getRankPathTo(destination);
        }
    }

    /**
     * Returns the positions between this position and the other position. (for
     * bishops and queens)
     * 
     * @param other the other position
     * @return the positions between this position and the other position excluding
     *         the destination
     */
    public Position[] getDiagonalPathTo(Position destination) {
        if (!this.isOnSameDiagonalAs(destination)) {
            throw new IllegalArgumentException("Positions must be on the same diagonal");
        }

        int xDirection = this.rank < destination.rank ? 1 : -1;
        int yDirection = this.file < destination.file ? 1 : -1;

        Position[] path = new Position[Math.abs(this.rank - destination.rank) - 1];

        int x = this.rank + xDirection;
        int y = this.file + yDirection;
        for (int i = 0; i < path.length; i++) {
            path[i] = new Position(x, y);
            x += xDirection;
            y += yDirection;
        }

        return path;
    }

    /**
     * Returns the positions between this position and the other position.
     * (only for queens)
     * 
     * @param other the other position
     * @return the positions between this position and the other position excluding
     *         the destination
     */
    public Position[] getPathTo(Position destination) {
        if (this.isOnSameDiagonalAs(destination)) {
            return this.getDiagonalPathTo(destination);
        } else if (this.isOnSameFileAs(destination) || this.isOnSameRankAs(destination)) {
            return this.getLinearPathTo(destination);
        } else {
            throw new IllegalArgumentException("Positions must be on the same diagonal, file, or rank");
        }
    }

    public int getRank() {
        return this.rank;
    }

    public int getFile() {
        return this.file;
    }

    @Override
    public boolean equals(Object obj) {
        Position other = (Position) obj;
        return this.rank == other.rank && this.file == other.file;
    }

    @Override
    public String toString() {
        return (char) ('a' + this.file) + "" + (8 - this.rank);
    }
}
