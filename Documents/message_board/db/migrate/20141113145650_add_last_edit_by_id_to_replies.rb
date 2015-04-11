class AddLastEditByIdToReplies < ActiveRecord::Migration
  def change
    add_column :replies, :last_edit_by_id, :integer
  end
end
