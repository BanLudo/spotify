import { Component, input, InputSignal } from "@angular/core";
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
}
