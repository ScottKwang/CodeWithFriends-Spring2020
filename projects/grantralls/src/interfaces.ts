export interface userData {
	phoneNumber: string,
	options: optionData
};

interface optionData {
	position: string,
	city: string,
	state: string,
	salary: string,
	searchTerm: string[]
}
