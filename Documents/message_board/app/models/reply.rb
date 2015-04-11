# Reply
class Reply < ActiveRecord::Base
  belongs_to :account
  delegate :name, :to => :account, :prefix => true
  belongs_to :topic
  belongs_to :last_edit_by, class_name: 'Account',
    foreign_key: 'last_edit_by_id', validate: true
  has_and_belongs_to_many :replies, dependent: :destroy

  validates :account, presence: true

  def to_string
    ' reply owner: ' + account.name +
    "\n reply text: " + text
  end
end
