class AddMovieId < ActiveRecord::Migration[6.0]
  def change
    add_column :events, :movie, :string
  end
end
