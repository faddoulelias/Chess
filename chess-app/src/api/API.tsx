import Board from "./Board";

export type MovesArray = string[];
interface AvailableMovesResponseJSON {
    moves: MovesArray;
}

export default class API {
    static URL: string = "http://127.0.0.1:8000";

    public static async getChessBoard(): Promise<Board> {
        let requestOptions: RequestInit = {
            method: 'GET',
            redirect: 'follow'
        };

        console.log("fetching from " + this.URL + "/chess/board");
        let res: Response = await fetch(API.URL + "/chess/board", requestOptions);
        let data: Board = new Board(await res.json());

        return data;
    }

    public static async getAllowableMoves(position: string): Promise<MovesArray> {
        let requestOptions: RequestInit = {
            method: 'GET',
            redirect: 'follow'
        };

        console.log("fetching from " + this.URL + "/chess/moves?" + position);
        let res: Response = await fetch(API.URL + "/chess/moves?" + position, requestOptions);
        let data: AvailableMovesResponseJSON = await res.json();

        return data.moves;
    }

    public static async movePiece(from: string, to: string): Promise<void> {
        let headers = new Headers();
        headers.append('Content-Type', 'text/plain');
        let raw = from + "," + to;

        let requestOptions: RequestInit = {
            method: 'POST',
            headers: headers,
            body: raw,
            redirect: 'follow',
        };

        let res: Response = await fetch(API.URL + "/chess/move", requestOptions);
    }
}