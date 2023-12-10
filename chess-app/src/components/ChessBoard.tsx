import React from 'react'
import API, { MovesArray } from '../api/API';
import Board, { Piece } from '../api/Board';
import ChessBoardSquare, { positionToString } from './ChessBoardSquare';

interface ChessBoardProps {
    id: number;
}

export default function ChessBoard(props: ChessBoardProps) {
    const [chessBoard, setChessBoard] = React.useState<any>(new Board([]));
    const [allowableMoves, setAllowableMoves] = React.useState<MovesArray>([]);
    const [selectedSquare, setSelectedSquare] = React.useState<string>("");

    async function handleClick(pos: string, piece: Piece | null) {
        if (allowableMoves.includes(pos)) {
            await API.movePiece(props.id, selectedSquare, pos);
            let newChessBoard = await API.getChessBoard(props.id);
            setChessBoard(newChessBoard);
            setAllowableMoves([]);
            setSelectedSquare("");
            return;
        }

        if (piece === null || selectedSquare === pos ||
            piece.color !== chessBoard.getUserColor()
        ) {
            setAllowableMoves([]);
            setSelectedSquare("");
            return;
        }


        let newAllowableMoves = await API.getAllowableMoves(props.id, pos);
        setAllowableMoves(newAllowableMoves);
        setSelectedSquare(pos);
    }


    React.useEffect(() => {
        API.getChessBoard(props.id).then((data) => {
            setChessBoard(data);
        });
    }, []);

    React.useEffect(() => {
        const interval = setInterval(async () => {
            let updated = await API.hasUpdated(props.id, chessBoard.lastUpdate);
            if (updated) {
                let newChessBoard = await API.getChessBoard(props.id);
                setChessBoard(newChessBoard);
            }
        }, 1000);

        return () => clearInterval(interval);
    }, [chessBoard]);

    return (
        <div>
            <h2> {
                chessBoard.isUserTurn() ? "Your turn" : "Opponent's turn"
            } </h2>
            <table
                style={{
                    borderCollapse: "collapse",
                    borderSpacing: "0",
                    border: "1px solid #000000",
                    margin: "auto",
                }}
            >
                <tbody>
                    {
                        chessBoard.toPiecesArray().map((row: (Piece | null)[], i: number) => {
                            return (
                                <tr key={i}>
                                    <td style={{ textAlign: "center", width: "1em" }}>{8 - i}</td>
                                    {
                                        row.map((piece: Piece | null, j: number) => {
                                            return (
                                                <td key={j}>
                                                    <ChessBoardSquare piece={piece} position={{ rank: i, file: j }}
                                                        onClick={(pos) => handleClick(pos, piece)}
                                                        allowable={allowableMoves.includes(
                                                            positionToString({ rank: i, file: j })
                                                        )}
                                                    />
                                                </td>
                                            )
                                        })
                                    }
                                </tr>
                            )
                        })
                    }
                    <tr>
                        <td></td>
                        <td style={{ textAlign: "center", width: "1em" }}>A</td>
                        <td style={{ textAlign: "center", width: "1em" }}>B</td>
                        <td style={{ textAlign: "center", width: "1em" }}>C</td>
                        <td style={{ textAlign: "center", width: "1em" }}>D</td>
                        <td style={{ textAlign: "center", width: "1em" }}>E</td>
                        <td style={{ textAlign: "center", width: "1em" }}>F</td>
                        <td style={{ textAlign: "center", width: "1em" }}>G</td>
                        <td style={{ textAlign: "center", width: "1em" }}>H</td>
                    </tr>
                </tbody>
            </table>
        </div>
    )
}
