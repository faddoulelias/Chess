import Board from "./Board";

export type MovesArray = string[];
interface AvailableMovesResponseJSON {
    moves: MovesArray;
}

export default class API {
    static URL: string = "http://127.0.0.1:8000";

    public static async authenticateToken(token: string): Promise<boolean> {
        let headers = new Headers();
        headers.append('Content-Type', 'text/plain');
        headers.append('Authorization', 'Basic ' + token);

        let requestOptions: RequestInit = {
            method: 'GET',
            redirect: 'follow',
            headers: headers
        };

        let res: Response = await fetch(API.URL + "/auth", requestOptions);

        return res.status === 200;
    }


    public static async authenticate(username: string, password: string): Promise<boolean> {
        const token: string = btoa(username + ":" + password);
        return await this.authenticateToken(token);
    }

    public static async getChessBoard(gameId: number): Promise<Board> {
        let requestOptions: RequestInit = {
            method: 'GET',
            redirect: 'follow'
        };

        console.log("fetching from " + this.URL + "/chess/status" + "?" + gameId);
        let res: Response = await fetch(API.URL + "/chess/status" + "?" + gameId, requestOptions);
        let data = await res.json();
        let board = new Board(data.board, data.lastUpdate, data.whitePlayer, data.blackPlayer, data.turn);

        return board;
    }

    public static async getAllowableMoves(gameId: number, position: string): Promise<MovesArray> {
        let requestOptions: RequestInit = {
            method: 'GET',
            redirect: 'follow'
        };

        console.log("fetching from " + this.URL + "/chess/moves?" + gameId + "&" + position);
        let res: Response = await fetch(API.URL + "/chess/moves?" + gameId + "&" + position, requestOptions);
        let data: AvailableMovesResponseJSON = await res.json();

        return data.moves;
    }

    public static async movePiece(gamedId: number, from: string, to: string): Promise<void> {
        let headers = new Headers();
        headers.append('Content-Type', 'text/plain');
        headers.append('Authorization', 'Basic ' + localStorage.getItem('token'));
        let raw = gamedId.toString() + "," + from + "," + to;

        let requestOptions: RequestInit = {
            method: 'POST',
            headers: headers,
            body: raw,
            redirect: 'follow'
        };

        let res: Response = await fetch(API.URL + "/chess/move", requestOptions);
    }

    public static async hasUpdated(gameId: number, lastUpdate: number): Promise<boolean> {
        let requestOptions: RequestInit = {
            method: 'GET',
            redirect: 'follow'
        };

        let res: Response = await fetch(API.URL + "/chess/update?" + gameId + "&" + lastUpdate, requestOptions);
        let data: string = await res.text();

        return data === "true";
    }
}