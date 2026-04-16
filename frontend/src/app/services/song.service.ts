import { HttpClient, HttpErrorResponse, HttpParams } from "@angular/common/http";
import { computed, inject, Injectable, signal, WritableSignal } from "@angular/core";
import { ReadSong, SaveSong } from "./model/song.model";
import { State } from "./model/state.model";
import { environment } from "../../environments/environment.development";
import { catchError, map, Observable, of } from "rxjs";

@Injectable({
	providedIn: "root",
})
export class SongService {
	http = inject(HttpClient);

	private add$: WritableSignal<State<SaveSong, HttpErrorResponse>> = signal(
		State.Builder<SaveSong, HttpErrorResponse>().forInit().build()
	);
	addSig = computed(() => this.add$());

	private getAll$: WritableSignal<State<Array<ReadSong>, HttpErrorResponse>> = signal(
		State.Builder<Array<ReadSong>, HttpErrorResponse>().forInit().build()
	);
	getAllSig = computed(() => this.getAll$());

	add(song: SaveSong): void {
		const formData = new FormData();
		formData.append("cover", song.cover!);
		formData.append("file", song.file!);
		const clone = structuredClone(song);
		clone.file = undefined;
		clone.cover = undefined;
		formData.append("dto", JSON.stringify(clone));

		this.http.post<SaveSong>(`${environment.API_URL}/api/songs`, formData).subscribe({
			next: (saveSong: SaveSong) =>
				this.add$.set(State.Builder<SaveSong, HttpErrorResponse>().forSuccess(saveSong).build()),
			error: (err) =>
				this.add$.set(State.Builder<SaveSong, HttpErrorResponse>().forError(err).build()),
		});
	}

	reset(): void {
		this.add$.set(State.Builder<SaveSong, HttpErrorResponse>().forInit().build());
	}

	getAll(): void {
		this.http.get<Array<ReadSong>>(`${environment.API_URL}/api/songs`).subscribe({
			next: (songs: ReadSong[]) =>
				this.getAll$.set(
					State.Builder<Array<ReadSong>, HttpErrorResponse>().forSuccess(songs).build()
				),
			error: (err) =>
				this.getAll$.set(State.Builder<Array<ReadSong>, HttpErrorResponse>().forError(err).build()),
		});
	}

	search(newSearchterm: string): Observable<State<Array<ReadSong>, HttpErrorResponse>> {
		const queryParam = new HttpParams().set("term", newSearchterm);

		return this.http
			.get<Array<ReadSong>>(`${environment.API_URL}/api/songs/search`, { params: queryParam })
			.pipe(
				map((songs: ReadSong[]) =>
					State.Builder<Array<ReadSong>, HttpErrorResponse>().forSuccess(songs).build()
				),
				catchError((err) =>
					of(State.Builder<Array<ReadSong>, HttpErrorResponse>().forError(err).build())
				)
			);
	}

	constructor() {}
}
