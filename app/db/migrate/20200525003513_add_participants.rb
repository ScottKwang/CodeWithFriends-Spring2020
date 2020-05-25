class AddParticipants < ActiveRecord::Migration[6.0]
  def change
    create_table :participants do |t|
       t.belongs_to :event
       t.belongs_to :user
     end
  end
end
