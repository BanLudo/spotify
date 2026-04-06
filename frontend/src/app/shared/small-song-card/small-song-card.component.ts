import { Component, EventEmitter, input, InputSignal, Output } from "@angular/core";
import { ReadSong } from "../../services/model/song.model";

@Component({
	selector: "app-small-song-card",
	standalone: true,
	imports: [],
	templateUrl: "./small-song-card.component.html",
	styleUrl: "./small-song-card.component.scss",
})
export class SmallSongCardComponent {
	song: InputSignal<ReadSong> = input.required<ReadSong>();

	@Output() songToPlay$: EventEmitter<ReadSong> = new EventEmitter<ReadSong>();

	play(): void {
		this.songToPlay$.emit(this.song());
	}
}
