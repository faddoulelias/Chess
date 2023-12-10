type BoardArray = number[];

export enum PieceColor {
    White = "white",
    Black = "black"
}

export enum PieceType {
    Pawn = "pawn",
    Knight = "knight",
    Bishop = "bishop",
    Rook = "rook",
    Queen = "queen",
    King = "king"
}

export class Piece {
    color: PieceColor;
    type: PieceType;

    constructor(color: PieceColor, type: PieceType) {
        this.color = color;
        this.type = type;
    }

    static fromInt(piece: number): Piece | null {
        if (piece === 0) {
            return null;
        }

        let newPiece: Piece = new Piece(PieceColor.White, PieceType.Pawn);
        newPiece.color = piece > 0 ? PieceColor.White : PieceColor.Black;
        switch (Math.abs(piece)) {
            case 1:
                newPiece.type = PieceType.Bishop;
                break;
            case 2:
                newPiece.type = PieceType.King;
                break;
            case 3:
                newPiece.type = PieceType.Knight;
                break;
            case 4:
                newPiece.type = PieceType.Pawn;
                break;
            case 5:
                newPiece.type = PieceType.Queen;
                break;
            case 6:
                newPiece.type = PieceType.Rook;
                break;
        }

        return newPiece;
    }

    toString(): string {
        let color: string = this.color === PieceColor.White ? "White" : "Black";
        let type: string = "";
        switch (this.type) {
            case PieceType.Pawn:
                type = "Pawn";
                break;
            case PieceType.Knight:
                type = "Knight";
                break;
            case PieceType.Bishop:
                type = "Bishop";
                break;
            case PieceType.Rook:
                type = "Rook";
                break;
            case PieceType.Queen:
                type = "Queen";
                break;
            case PieceType.King:
                type = "King";
                break;
        }
        return color + " " + type;
    }
}

export default class Board {
    lastUpdate: number;
    board: BoardArray;

    constructor(board: BoardArray, lastUpdate: number = new Date().getTime()) {
        this.lastUpdate = lastUpdate;
        this.board = board;
    }

    public getPieceAt(x: number, y: number): Piece | null {
        let piece: Piece | null = Piece.fromInt(this.board[x + y * 8]);
        return piece;
    }

    public toPiecesArray(): (Piece | null)[][] {
        let pieces: (Piece | null)[][] = [];
        for (let y = 0; y < 8; y++) {
            pieces.push([]);
            for (let x = 0; x < 8; x++) {
                pieces[y].push(this.getPieceAt(x, y));
            }
        }
        return pieces;
    }
}