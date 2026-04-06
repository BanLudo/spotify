import { HttpClient, HttpErrorResponse, HttpParams } from "@angular/common/http";
import { computed, inject, Injectable, signal, WritableSignal } from "@angular/core";
import { ReadSong, SongContent } from "./model/song.model";
import { State } from "./model/state.model";
import { environment } from "../../environments/environment.development";

@Injectable({
	providedIn: "root",
})
export class SongContentService {
	http = inject(HttpClient);

	// créer/maintenir une file d'attente de musique à jouer
	private queueToPlay$: WritableSignal<Array<ReadSong>> = signal([]);
	queueToPlay = computed(() => this.queueToPlay$());

	// recup le contenu d'une musique avant de l'écouter.. la musique courante
	private play$: WritableSignal<State<SongContent, HttpErrorResponse>> = signal(
		State.Builder<SongContent, HttpErrorResponse>().forInit().build()
	);
	playNewSong = computed(() => this.play$());

	createNewQueue(firstSong: ReadSong, songsToPlay: Array<ReadSong>): void {
		songsToPlay = songsToPlay.filter((song) => song.publicId !== firstSong.publicId);

		if (songsToPlay) {
			songsToPlay.unshift(firstSong); // first song à l'avant du tableau
		}
		this.queueToPlay$.set(songsToPlay);
	}

	fetchNextSong(songToPlay: SongContent): void {
		const queryParam = new HttpParams().set("publicId", songToPlay.publicId!);
		this.http
			.get<SongContent>(`${environment.API_URL}/api/songs/get-content`, { params: queryParam })
			.subscribe({
				next: (songContent) => {
					this.mapReadSongToSongContent(songContent, songToPlay);
					this.play$.set(
						State.Builder<SongContent, HttpErrorResponse>().forSuccess(songContent).build()
					);
				},
				error: (err) => {
					this.play$.set(State.Builder<SongContent, HttpErrorResponse>().forError(err).build());
				},
			});
	}

	constructor() {}

	private mapReadSongToSongContent(songContent: SongContent, songToPlay: ReadSong) {
		songContent.cover = songToPlay.cover;
		songContent.coverContentType = songToPlay.coverContentType;
		songContent.title = songToPlay.title;
		songContent.author = songToPlay.author;
		songContent.favorite = songToPlay.favorite;
	}
}
