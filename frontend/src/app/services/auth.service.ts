import { Location } from "@angular/common";
import { HttpClient, HttpErrorResponse, HttpStatusCode } from "@angular/common/http";
import { computed, inject, Injectable, signal, WritableSignal } from "@angular/core";
import { State } from "./model/state.model";
import { User } from "./model/user.model";
import { environment } from "../../environments/environment.development";
import { delay, Observable, of, tap } from "rxjs";

@Injectable({
	providedIn: "root",
})
export class AuthService {
	http: HttpClient = inject(HttpClient);
	location: Location = inject(Location);
	notConnected: string = "NOT_CONNECTED";

	/*-----------------------------------*/
	mockbackend: boolean = true; //active le mock simule un appel backend
	/*---------------------------------------*/

	// signal pour stocker l'état utilisateur
	private fetchUser$: WritableSignal<State<User, HttpErrorResponse>> = signal(
		State.Builder<User, HttpErrorResponse>().forSuccess({ email: this.notConnected }).build()
	);
	fetchUser = computed(() => this.fetchUser$());

	/** -------------------------------
	 * Méthode fetch() existante, utilisée par HeaderComponent etc.
	 * ------------------------------- */
	fetch(): void {
		if (this.mockbackend) {
			//simule un appel backend
			of({ email: "mockuser@example.com" } as User)
				.pipe(delay(200))
				.subscribe((user) => {
					this.fetchUser$.set(State.Builder<User, HttpErrorResponse>().forSuccess(user).build());
				});
			return;
		}

		//vrai backend à remettre
		this.http.get<User>(`${environment.API_URL}/api/get-authenticated-user`).subscribe({
			next: (user: User) =>
				this.fetchUser$.set(State.Builder<User, HttpErrorResponse>().forSuccess(user).build()),
			error: (err: HttpErrorResponse) => {
				if (err.status === HttpStatusCode.Unauthorized && this.isAuthenticated()) {
					this.fetchUser$.set(
						State.Builder<User, HttpErrorResponse>()
							.forSuccess({ email: this.notConnected })
							.build()
					);
				} else {
					this.fetchUser$.set(State.Builder<User, HttpErrorResponse>().forError(err).build());
				}
			},
		});
	}

	/** -------------------------------
	 * Nouvelle version Observable pour chaîner (ex: logout)
	 * ------------------------------- */
	fetchAsObservable(): Observable<User> {
		return this.http.get<User>(`${environment.API_URL}/api/get-authenticated-user`).pipe(
			tap({
				next: (user: User) =>
					this.fetchUser$.set(State.Builder<User, HttpErrorResponse>().forSuccess(user).build()),
				error: (err: HttpErrorResponse) =>
					this.fetchUser$.set(State.Builder<User, HttpErrorResponse>().forError(err).build()),
			})
		);
	}

	/** -------------------------------
	 * Vérifie si l'utilisateur est connecté
	 * ------------------------------- */
	isAuthenticated(): boolean {
		if (this.fetchUser$().value) {
			return this.fetchUser$().value!.email !== this.notConnected;
		} else {
			return false;
		}
	}

	/** -------------------------------
	 * Redirection vers le login OAuth2
	 * ------------------------------- */
	login(): void {
		window.location.href = `${window.location.origin}${this.location.prepareExternalUrl(
			"oauth2/authorization/okta"
		)}`;
	}

	/** -------------------------------
	 * Logout SPA propre
	 * Appelle d'abord fetchAsObservable() pour préparer CSRF + session
	 * ------------------------------- */
	logout(): void {
		this.fetchAsObservable().subscribe({
			next: () => this.executeLogout(), //si session + csrf token ready
			error: () => this.executeLogout(), // mm si le fetch échoue
		});
	}

	private executeLogout(): void {
		this.http.post(`${environment.API_URL}/api/logout`, {}, { withCredentials: true }).subscribe({
			next: (response: any) => {
				this.fetchUser$.set(
					State.Builder<User, HttpErrorResponse>().forSuccess({ email: this.notConnected }).build()
				);
				location.href = response.logoutUrl;
			},
			error: (err) => console.error("Logout failed", err),
		});
	}

	/*
	logout(): void {
		this.http.post(`${environment.API_URL}/api/logout`, {}, { withCredentials: true }).subscribe({
			next: (response: any) => {
				this.fetchUser$.set(
					State.Builder<User, HttpErrorResponse>().forSuccess({ email: this.notConnected }).build()
				);
				location.href = response.logoutUrl;
			},
		});
	} */
}
