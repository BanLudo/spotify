import { Component, inject, signal } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { SmallSongCardComponent } from "../shared/small-song-card/small-song-card.component";
import { SongService } from "../services/song.service";
import { SongContentService } from "../services/song-content.service";
import { ToastService } from "../services/toast.service";
import { ReadSong } from "../services/model/song.model";
import {
	debounce,
	debounceTime,
	distinctUntilChanged,
	filter,
	interval,
	of,
	switchMap,
	tap,
} from "rxjs";
import { takeUntilDestroyed, toObservable } from "@angular/core/rxjs-interop";
import { HttpErrorResponse } from "@angular/common/http";
import { State } from "../services/model/state.model";

@Component({
	selector: "app-search",
	standalone: true,
	imports: [FormsModule, FontAwesomeModule, SmallSongCardComponent],
	templateUrl: "./search.component.html",
	styleUrl: "./search.component.scss",
})
export class SearchComponent {
	/* searchTerm: string = "";

	private songService = inject(SongService);
	private songContentService = inject(SongContentService);
	private toastService = inject(ToastService);

	songsResult: Array<ReadSong> = [];

	isSearching: boolean = false;

	onSearch(newSearchTerm: string): void {
		this.searchTerm = newSearchTerm;
		of(newSearchTerm)
			.pipe(
				tap((newSearchTerm: string) => this.resetResultIfEmptyTerm(newSearchTerm)),
				filter((newSearchTerm: string) => newSearchTerm.length > 0),
				debounce(() => interval(300)),
				tap(() => (this.isSearching = true)),
				switchMap((newSearchTerm: string) => this.songService.search(newSearchTerm))
			)
			.subscribe({
				next: (searchState) => this.onNext(searchState),
			});
	}

	private onNext(searchState: State<Array<ReadSong>, HttpErrorResponse>): void {
		this.isSearching = false;
		if (searchState.status === "OK") {
			this.songsResult = searchState.value!;
		} else if (searchState.status === "ERROR") {
			this.toastService.show("An error occured while searching", "DANGER");
		}
	}

	private resetResultIfEmptyTerm(newSearchTerm: string): void {
		if (newSearchTerm.length === 0) {
			this.songsResult = [];
		}
	}

	onPlay(firstSong: ReadSong): void {
		this.songContentService.createNewQueue(firstSong, this.songsResult);
	} */

	/*------------ signals---------------------------------*/
	private songService = inject(SongService);
	private songContentService = inject(SongContentService);
	private toastService = inject(ToastService);

	searchTerm = signal("");
	songsResult = signal<Array<ReadSong>>([]);
	isSearching = signal(false);

	constructor() {
		toObservable(this.searchTerm)
			.pipe(
				tap((term: string) => {
					if (term.length === 0) {
						this.songsResult.set([]);
						this.isSearching.set(false);
					}
				}),
				filter((term: string) => term.length > 0),
				debounceTime(300),
				distinctUntilChanged(),
				tap(() => this.isSearching.set(true)),
				switchMap((term: string) => this.songService.search(term)),
				takeUntilDestroyed()
			)
			.subscribe({
				next: (searchState) => {
					this.isSearching.set(false);

					if (searchState.status === "OK") {
						this.songsResult.set(searchState.value ?? []);
					} else if (searchState.status === "ERROR") {
						this.toastService.show("An error occurred while searching", "DANGER");
					}
				},
				error: () => {
					this.isSearching.set(false);
					this.toastService.show("An error occurred while searching", "DANGER");
				},
			});
	}

	onSearch(newSearchTerm: string): void {
		this.searchTerm.set(newSearchTerm);
	}

	onPlay(firstSong: ReadSong): void {
		this.songContentService.createNewQueue(firstSong, this.songsResult());
	}
}
