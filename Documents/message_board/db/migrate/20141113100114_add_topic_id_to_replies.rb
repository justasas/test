class AddTopicIdToReplies < ActiveRecord::Migration
  def change
    add_column :replies, :topic_id, :integer
  end
end
