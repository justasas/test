class AddReplyIdToReplies < ActiveRecord::Migration
  def change
    add_column :replies, :reply_id, :integer
  end
end
