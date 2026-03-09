import { animate, style, transition, trigger } from "@angular/animations";
import { Component, input, InputSignal, OnInit } from "@angular/core";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { ReadSong } from "../../services/model/song.model";
import { faL } from "@fortawesome/free-solid-svg-icons";

@Component({
	selector: "app-song-card",
	standalone: true,
	imports: [FontAwesomeModule],
	templateUrl: "./song-card.component.html",
	styleUrl: "./song-card.component.scss",
	animations: [
		trigger("inOutAnimation", [
			transition(":enter", [
				style({ tansform: "translateY(10px)", opacity: 0 }),
				animate(".2s ease-out", style({ transform: "translateY(0px)", opacity: 1 })),
			]),
			transition(":leave", [
				style({ tansform: "translateY(0px)", opacity: 1 }),
				animate(".2s ease-in", style({ transform: "translateY(10px)", opacity: 0 })),
			]),
		]),
	],
})
export class SongCardComponent implements OnInit {
	song: InputSignal<ReadSong> = input.required<ReadSong>();

	songDisplay: ReadSong = {
		favorite: false,
		displayPlay: false,
	};

	ngOnInit(): void {
		this.songDisplay = this.song();
	}

	onHoverPlay(displayIcon: boolean): void {
		this.songDisplay.displayPlay = displayIcon;
	}
}
