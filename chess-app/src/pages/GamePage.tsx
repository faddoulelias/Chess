import React from 'react'
import ChessBoard from '../components/ChessBoard';

interface GamePageProps {
    id: number;
}

export default function GamePage(props: GamePageProps) {
    return (
        <div>
            <div className="App">
                <h1>Chessboard</h1>
                <ChessBoard id={props.id}></ChessBoard>
            </div >
        </div>
    )
}
