import React from 'react'
import API, { MovesArray } from '../api/API';
import Board, { Piece } from '../api/Board';
import ChessBoardSquare, { positionToString } from './ChessBoardSquare';

export default function ChessBoard() {
    const [chessBoard, setChessBoard] = React.useState<any>(new Board([]));
    const [allowableMoves, setAllowableMoves] = React.useState<MovesArray>([]);
    const [selectedSquare, setSelectedSquare] = React.useState<string>("");

    async function handleClick(pos: string, piece: Piece | null) {
        if (allowableMoves.includes(pos)) {
            await API.movePiece(67, selectedSquare, pos);
            let newChessBoard = await API.getChessBoard(67);
            setChessBoard(newChessBoard);
            setAllowableMoves([]);
            setSelectedSquare("");
            return;
        }

        if (piece === null || selectedSquare === pos) {
            setAllowableMoves([]);
            setSelectedSquare("");
            return;
        }


        let newAllowableMoves = await API.getAllowableMoves(67, pos);
        setAllowableMoves(newAllowableMoves);
        setSelectedSquare(pos);
    }


    React.useEffect(() => {
        API.getChessBoard(67).then((data) => {
            setChessBoard(data);
        });
    }, []);

    React.useEffect(() => {
        const interval = setInterval(async () => {
            let updated = await API.hasUpdated(67, chessBoard.lastUpdate);
            if (updated) {
                let newChessBoard = await API.getChessBoard(67);
                setChessBoard(newChessBoard);
            }
        }, 1000);

        return () => clearInterval(interval);
    }, [chessBoard]);

    return (
        <div>
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
