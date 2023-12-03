import {
    FaChessBishop, FaChessKing, FaChessKnight,
    FaChessPawn, FaChessQueen, FaChessRook
} from "react-icons/fa";
import { Piece, PieceType, PieceColor } from '../api/Board';

interface Position {
    rank: number;
    file: number;
}

interface Props {
    piece: Piece | null;
    position: Position;
    allowable?: boolean;
    onClick?: (positionString: string, piece: Piece | null) => void;
}

export function positionToString(position: Position): string {
    return String.fromCharCode(97 + position.file) + (8 - position.rank);
}

function getPieceIcon(props: Props): JSX.Element {
    if (props.piece === null) {
        return <></>;
    }

    let pieceColor: string = props.piece.color === PieceColor.White ? "#f8f8f8" : "#464646";;
    let style = {
        color: pieceColor,
        fontSize: "2.5em",
        filter: "drop-shadow(0px 0px 1px #000000)",
    };

    switch (props.piece.type) {
        case PieceType.Knight:
            return <FaChessKnight style={style} />;
        case PieceType.Bishop:
            return <FaChessBishop style={style} />;
        case PieceType.Rook:
            return <FaChessRook style={style} />;
        case PieceType.Queen:
            return <FaChessQueen style={style} />;
        case PieceType.King:
            return <FaChessKing style={style} />;
        case PieceType.Pawn:
            return <FaChessPawn style={style} />;
        default:
            return <></>;
    }
}

export default function ChessBoardSquare(props: Props): JSX.Element {
    let backgroundColor: string = (props.position.rank + props.position.file) % 2 === 0 ? "#769656" : "#eeeed2";
    if (props.allowable) {
        backgroundColor = "#a3c2a3";
    }
    return (
        <div
            className="chess-board-square"
            style={{
                backgroundColor: backgroundColor,
                width: "4em",
                height: "4em",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                border: "1px solid #444444",
            }}
            onClick={() => {
                if (props.onClick !== undefined) {
                    props.onClick(positionToString(props.position), props.piece);
                }
            }}
        >
            {getPieceIcon(props)}
        </div>
    )
}
